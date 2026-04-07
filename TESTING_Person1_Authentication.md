# Person 1 — Authentication Testing Guide

## Setup — seed the database first

In `src/Controller/Main.java`, uncomment the sample data block, run the app once to seed users, then comment it back out and run again.

Sample credentials: `admin / qwerty1234A!` (role 5), `manager / qwerty1234A!` (role 4), `staff / qwerty1234A!` (role 3), `client1 / qwerty1234A!` (role 2).

---

## 1 — BCrypt password hashing

Open `database.db` with DB Browser for SQLite.  
Check the `password` column — every value should start with `$2a$12$`, not plain text.

**PASS** ✓

---

## 2 — Login: wrong password (generic error)

- Enter a valid username + wrong password → **"Invalid username and/or password."**
- Enter a non-existent username + any password → same message (no hint that the user doesn't exist)

**PASS** ✓

---

## 3 — Account lockout after 5 failures

Use any valid account (e.g. `client1`):
1. Enter wrong password 4 times → each time you get the generic error
2. Enter wrong password a **5th time** → message changes to **"This account has been disabled."**
3. Verify in DB: `role` column for that user is now `1`
4. Try logging in with the **correct** password → still get "account has been disabled"

**PASS** ✓

---

## 4 — Disabled account blocked at login

- Set any user's role to `1` directly in the DB
- Try to log in with correct credentials → **"This account has been disabled."** (before password is even checked)

**PASS** ✓

---

## 5 — Last login display

1. Log in with `admin` → the home panel shows **"Welcome! This is your first login."**
2. Log out, log in again → shows the **previous** login timestamp + `SUCCESS`
3. Log out, enter wrong password, then log in correctly → shows the previous attempt's timestamp + `FAILED`

**PASS** ✓

---

## 6 — Registration validation

Go to Register screen and try:

| Input | Expected | Result |
|-------|----------|--------|
| Username `ab` (too short) | "3–30 characters" error | PASS ✓ |
| Username `hello world` (space) | same error | PASS ✓ |
| Password `password` (no uppercase/digit/special) | policy error | PASS ✓ |
| Password `Short1!` (7 chars) | policy error | PASS ✓ |
| Valid username that already exists | "username already taken" error | PASS ✓ |
| Password `qwerty1234A!` + wrong confirm | "Passwords do not match" | PASS ✓ |
| All valid | Success → redirected to login | PASS ✓ |

---

## 7 — Re-authentication before admin actions

Log in as `admin`, go to **USERS**, select any user and click **EDIT ROLE / DELETE / LOCK / CHANGE PASS**:
- A password prompt appears
- Enter wrong password → operation is cancelled
- Enter correct password → action proceeds

**PASS** ✓

---

## 8 — Timing defense

When a non-existent username is entered, the code runs a dummy `BCrypt.checkpw()` before returning the error, ensuring response time is indistinguishable from a real failed attempt. See `Login.java` lines 21–22 (dummy hash) and line 128 (dummy check).

**PASS** ✓ (verified by code review)

---

## Quick sanity checklist

- [x] Passwords in DB are all `$2a$12$...` hashes
- [x] Wrong password gives generic error (not "user not found")
- [x] 5th wrong attempt disables the account
- [x] Disabled account can't log in even with correct password
- [x] Last login info shows on home panel after second login
- [x] Registration rejects weak/short/duplicate usernames and passwords
- [x] Admin actions require password re-entry
- [x] Timing defense in place for non-existent usernames
