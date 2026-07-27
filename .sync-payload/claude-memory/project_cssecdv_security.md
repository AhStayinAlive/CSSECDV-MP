---
name: CSSECDV-MP Security Implementation
description: Full context of the CSSECDV academic security project — what was built, all file changes, dependencies, architecture decisions, and key notes for continuing work on the correct repo
type: project
---

## Project Overview

Academic project: **SECURITY Svcs** — a Java Swing desktop app (NetBeans, SQLite via JDBC, MVC pattern).
The task was to implement authentication security features as one person in a group assignment.

**Working directory when coded**: `c:\Users\stayin_alive\Documents\GitHub\CSSECDV-MP`
**Note from user**: This was the WRONG repo. Real work needs to go into a different repo. All changes described here need to be replicated there.

**Why:** CSSECDV (Secure Software Development) course machine project. Person roles: Person 1 = authentication (us), Person 2 = authorization/RBAC, Person 5 = logging.

---

## Role Codes (DO NOT CHANGE)
- Administrator = 5
- Manager = 4
- Staff = 3
- Client = 2
- Disabled = 1 ← auto-lockout target after 5 failed login attempts

---

## Dependency Added: jBCrypt

- Downloaded `jbcrypt-0.4.jar` from Maven Central (`org.mindrot.jbcrypt`)
- Placed in: project root AND `dist/lib/jbcrypt-0.4.jar`
- `nbproject/project.properties` — added to `javac.classpath` and `run.classpath`:
  ```
  file.reference.jbcrypt-0.4.jar=jbcrypt-0.4.jar
  javac.classpath=\
      ${file.reference.sqlite-jdbc-3.23.1.jar}:\
      ${file.reference.jbcrypt-0.4.jar}
  ```
- `.vscode/settings.json` created for VS Code Java extension:
  ```json
  { "java.project.referencedLibraries": ["sqlite-jdbc-3.23.1.jar", "jbcrypt-0.4.jar"] }
  ```
- Import: `import org.mindrot.jbcrypt.BCrypt;`
- Usage: `BCrypt.hashpw(password, BCrypt.gensalt(12))` and `BCrypt.checkpw(plain, hash)`

---

## All Files Modified

### `src/Model/User.java`
Added 3 new fields with full constructors, getters, setters:
- `int failedAttempts` (default 0)
- `String lastLoginTimestamp` (default "")
- `String lastLoginStatus` (default "")
New 8-arg constructor: `User(id, username, password, role, locked, failedAttempts, lastLoginTimestamp, lastLoginStatus)`
Old constructors kept as-is.

### `src/Controller/SQLite.java`
**Complete rewrite** — key changes:
- Added `import org.mindrot.jbcrypt.BCrypt` and `import java.sql.PreparedStatement`
- `createUserTable()` — now includes `failed_attempts INTEGER DEFAULT 0`, `last_login_timestamp TEXT`, `last_login_status TEXT`
- `migrateDatabase()` — NEW: `ALTER TABLE users ADD COLUMN` for each new column, silently catches duplicate-column errors (safe to run on existing DBs)
- `addUser(username, password)` — now BCrypt-hashes password, uses PreparedStatement
- `addUser(username, password, role)` — same
- ALL parameterized queries converted from string-concat Statement to PreparedStatement: `addHistory`, `addLogs`, `addProduct`, `removeUser`, `getProduct(String name)`
- `getUsers()` — now SELECTs and maps all 8 columns including new ones
- All catch blocks: changed from `System.out.print(ex)` / `ex.printStackTrace()` to `System.out.println("methodName error: " + ex.getMessage())`

**New methods (all PreparedStatement):**
- `getUserByUsername(String username)` → `User` or null
- `updatePassword(String username, String hashedPassword)`
- `incrementFailedAttempts(String username)`
- `resetFailedAttempts(String username)`
- `disableUser(String username)` → sets role=1
- `updateLastLogin(String username, String timestamp, String status)`
- `updateUserRole(String username, int role)` — for MgmtUser EDIT ROLE
- `setUserLocked(String username, int locked)` — for MgmtUser LOCK/UNLOCK (separate from role=1 disable)

### `src/View/Login.java`
- `passwordFld` changed from `JTextField` to `JPasswordField` (in GEN sections too)
- `DUMMY_HASH` static field: `BCrypt.hashpw("_timing_defense_placeholder_XyZ9!", BCrypt.gensalt(12))` — computed at class load
- Full `loginBtnActionPerformed` logic:
  1. Trim username, read password with `.getPassword()`, clear field immediately
  2. Empty guard → generic error
  3. `getUserByUsername(username)` → if null: dummy BCrypt check for timing, generic error
  4. If `user.getRole() == 1` → "This account has been disabled..."
  5. `BCrypt.checkpw(entered, user.getPassword())`
  6. On failure: `incrementFailedAttempts` → reload user → if `failedAttempts >= 5`: `disableUser` + disabled message; else generic error
  7. On success: capture `prevTimestamp`/`prevStatus` from user object BEFORE updating → `resetFailedAttempts` → `updateLastLogin(ts, "SUCCESS")` → re-fetch user → `frame.mainNav(user, prevTimestamp, prevStatus)`
- Error message is always **identical**: `"Invalid username and/or password."` — never distinguishes missing user vs wrong password
- Timestamp format: `LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))`

### `src/View/Register.java`
- `passwordFld` and `confpassFld` changed to `JPasswordField` (in GEN sections too)
- Static helper methods (also used by `MgmtUser`):
  - `isValidPassword(String)` — 8-64 chars, upper, lower, digit, special char from `!@#$%^&*()_+-=[]{}|;':\",./<>?\`~`
  - `isValidUsername(String)` — regex `[a-zA-Z0-9_]{3,30}`
- `registerBtnActionPerformed`: validates username → checks duplicate via `getUserByUsername` → validates password policy → checks confirm-match → calls `frame.registerAction(username, password)` → shows success dialog → `frame.loginNav()`
- Does NOT auto-login after registration

### `src/View/Frame.java`
- Added `public User sessionUser = null;` field
- `logoutBtnActionPerformed`: now also sets `sessionUser = null`
- `mainNav(User user, String prevTimestamp, String prevStatus)` — replaces old no-arg `mainNav()`:
  - Sets `sessionUser = user`
  - Routes to correct role panel based on `user.getRole()` (5=Admin, 4=Manager, 3=Staff, 2=Client)
  - Calls `xxxHomePnl.updateLastLogin(prevTimestamp, prevStatus)` then shows that panel
- `registerAction(String username, String password)` — now 2-arg (validation done in Register.java)
- `init()` — calls `xxxHomePnl.init(main.sqlite, this)` passing Frame reference

### `src/View/Home.java`
- Added `private String welcomeText` field
- Constructor stores `welcomeText`
- Added `setLastLoginInfo(String lastLoginTimestamp, String lastLoginStatus)`:
  - If null/empty → sets label to HTML with "Welcome! This is your first login."
  - Else → "Last login: [timestamp]  —  [status]"
  - Uses HTML in the existing `userLbl` JLabel — no layout changes needed

### `src/View/AdminHome.java`, `ManagerHome.java`, `StaffHome.java`, `ClientHome.java`
All four changed identically:
- Added `private Home home;` field
- `init(SQLite sqlite)` → `init(SQLite sqlite, Frame frame)` — passes frame to MgmtUser
- Now stores Home reference: `home = new Home("WELCOME ADMIN!", color); Content.add(home, "home");`
- Added `updateLastLogin(String timestamp, String status)` → delegates to `home.setLastLoginInfo(...)`
- `MgmtUser` constructed as `new MgmtUser(sqlite, frame)`

### `src/View/MgmtUser.java`
- Constructor changed to `MgmtUser(SQLite sqlite, Frame frame)`
- Added `public Frame frame` field
- Added `private boolean reAuthenticate()`:
  - Shows JOptionPane with masked `JPasswordField`
  - Verifies against `frame.sessionUser.getPassword()` using `BCrypt.checkpw`
  - Returns false if: no session, empty input, wrong password
- All 4 critical buttons now call `reAuthenticate()` first, then execute real DB operations:
  - **EDIT ROLE**: `sqlite.updateUserRole(username, newRole)` + `init()`
  - **DELETE**: confirm dialog first, then re-auth, then `sqlite.removeUser(username)` + `init()`
  - **LOCK/UNLOCK**: `sqlite.setUserLocked(username, toggle)` + `init()`
  - **CHANGE PASS**: re-auth → new password dialog (2×JPasswordField) → `Register.isValidPassword()` check → confirm match → `sqlite.updatePassword(username, BCrypt.hashpw(...))` + `init()`

### `src/Controller/Main.java`
- `init()` now always calls: `createNewDatabase()`, all `createXxxTable()`, then `migrateDatabase()` (idempotent)
- Seeding block still commented out; sample passwords updated to `qwerty1234A!` (meets policy: upper A, digit 1234, special !)

### `src/Model/History.java`, `src/Model/Logs.java`
- `ex.printStackTrace()` in constructor catch blocks replaced with `System.out.println("... error: " + ex.getMessage())`

---

## Architecture Decisions

1. **BCrypt work factor**: 12 (as specified)
2. **Timing defence**: `DUMMY_HASH` computed at class load via `BCrypt.hashpw(...)` — not a hardcoded string (which could be malformed)
3. **Last login display timing**: `prevTimestamp`/`prevStatus` captured from the User object BEFORE calling `updateLastLogin` — so the display always shows the PREVIOUS session
4. **Session storage**: `Frame.sessionUser` — single public field, set on login, cleared on logout
5. **Disabled vs Locked**: `disableUser()` sets `role=1` (auto-lockout, blocks login). `setUserLocked()` toggles `locked` column (manual admin action, separate mechanism). Login only checks `role==1`
6. **Re-auth scope**: All 4 MgmtUser buttons (Edit Role, Delete, Lock/Unlock, Change Pass)
7. **Password policy validation** lives in `Register.isValidPassword()` as a public static — reused by MgmtUser CHANGE PASS
8. **SQL injection prevention**: Every method that takes user input uses PreparedStatement. `Statement` is only used for fixed DDL and parameterless SELECTs.

---

## Sample Users (after seeding)

| Username | Password | Role |
|---|---|---|
| admin | qwerty1234A! | 5 (Administrator) |
| manager | qwerty1234A! | 4 (Manager) |
| staff | qwerty1234A! | 3 (Staff) |
| client1 | qwerty1234A! | 2 (Client) |
| client2 | qwerty1234A! | 2 (Client) |

All stored as BCrypt hashes starting with `$2a$12$`.

---

## First-Run Instructions (for the correct repo)

1. Copy/apply all changes above to the correct repo
2. Add `jbcrypt-0.4.jar` to project root and `dist/lib/`
3. Update `nbproject/project.properties` with the jar classpath entry
4. Delete any existing `database.db`
5. Uncomment seeding block in `Main.java`, run once, re-comment, rebuild

---

## What NOT to implement (other people's jobs)

- **Person 2**: Authorization / RBAC — do not hide/show nav buttons based on role
- **Person 5**: Logging to the `logs` table — do not add `sqlite.addLogs(...)` calls

**Why:** Group assignment. Interfering breaks their diffs and causes merge conflicts.
