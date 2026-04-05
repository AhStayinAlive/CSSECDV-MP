# Manual Testing Guide — CSSECDV Security Features

## Setup — seed the database first

In `src/Controller/Main.java`, uncomment the sample data block (lines 34–64), run the app once to seed users, then comment it back out and run again.

Sample credentials: `admin / qwerty1234A!` (role 5), `manager / qwerty1234A!` (role 4), `staff / qwerty1234A!` (role 3), `client1 / qwerty1234A!` (role 2).

---

## 1 — BCrypt password hashing

Open `database.db` with any SQLite viewer (e.g. DB Browser for SQLite).  
Check the `password` column — every value should start with `$2a$12$`, not plain text.

---

## 2 — Login: wrong password (generic error)

- Enter a valid username + wrong password → should say **"Invalid username and/or password."**
- Enter a non-existent username + any password → same message (no hint that the user doesn't exist)

---

## 3 — Account lockout after 5 failures

Use any valid account (e.g. `client1`):
1. Enter wrong password 4 times → each time you get the generic error
2. Enter wrong password a **5th time** → message changes to **"This account has been disabled."**
3. Verify in DB: `role` column for that user is now `1`
4. Try logging in with the **correct** password → still get "account has been disabled"

---

## 4 — Disabled account blocked at login

- Set any user's role to `1` directly in the DB
- Try to log in with correct credentials → **"This account has been disabled."** (before password is even checked)

---

## 5 — Last login display

1. Log in with `admin` → the home panel shows **"Welcome! This is your first login."**
2. Log out, log in again → shows the **previous** login timestamp + `SUCCESS`
3. Log out, enter wrong password, then log in correctly → shows the previous attempt's timestamp + `FAILED`

---

## 6 — Registration validation

Go to Register screen and try:

| Input | Expected |
|-------|----------|
| Username `ab` (too short) | "3–30 characters" error |
| Username `hello world` (space) | same error |
| Password `password` (no uppercase/digit/special) | policy error |
| Password `Short1!` (7 chars) | policy error |
| Valid username that already exists | "username already taken" error |
| Password `qwerty1234A!` + wrong confirm | "Passwords do not match" |
| All valid | Success → redirected to login |

---

## 7 — Re-authentication before admin actions

Log in as `admin`, go to **USERS**, select any user and click **EDIT ROLE / DELETE / LOCK / CHANGE PASS**:
- A password prompt appears
- Enter wrong password → operation is cancelled
- Enter correct password → action proceeds

---

## 8 — Timing defense (optional, harder to observe)

This is automatic — when a non-existent username is entered, the code runs a dummy `BCrypt.checkpw()` before returning the error. You won't see it visually, but the response time should be similar (~100–300ms) whether the username exists or not.

---

## Quick sanity checklist

- [ ] Passwords in DB are all `$2a$12$...` hashes
- [ ] Wrong password gives generic error (not "user not found")
- [ ] 5th wrong attempt disables the account
- [ ] Disabled account can't log in even with correct password
- [ ] Last login info shows on home panel after second login
- [ ] Registration rejects weak/short/duplicate usernames and passwords
- [ ] Admin actions require password re-entry
