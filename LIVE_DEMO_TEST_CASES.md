# Live Project Demo Test Cases

## Setup
- Seed the database first using the sample data block in `src/Controller/Main.java`.
- Sample credentials:
  - `admin / qwerty1234A!` (role 5)
  - `manager / qwerty1234A!` (role 4)
  - `staff / qwerty1234A!` (role 3)
  - `client1 / qwerty1234A!` (role 2)

---

## 1
Control Group
Authentication

Test Scenario
Attempt login with empty username or empty password.

Expected Outcome
The app shows the generic error message "Invalid username and/or password.", logs a `VALIDATION_FAILURE`, and remains on the login screen.

---

## 2
Control Group
Authentication

Test Scenario
Attempt login with a non-existent username or a valid username with the wrong password.

Expected Outcome
The app shows the generic error message "Invalid username and/or password.", logs `LOGIN_FAILURE`, and does not grant access.

---

## 3
Control Group
Authentication / Error Handling

Test Scenario
Enter an incorrect password five times for a valid user account.

Expected Outcome
On the 5th failed attempt, the app logs `ACCOUNT_LOCKED`, the user account is disabled in the database (role becomes `1`), and further login attempts do not proceed to a role home view.

---

## 4
Control Group
Authentication / Authorization

Test Scenario
Log in with each sample user and verify the home screen and available navigation buttons.

Expected Outcome
- `admin` sees the admin panel and admin-only sections.
- `manager` sees manager functions.
- `staff` sees staff product management functions.
- `client1` sees the client home and purchase flow only.

---

## 5
Control Group
Data Validation

Test Scenario
Attempt registration with invalid usernames such as `ab`, `hello world`, or symbols outside `[a-zA-Z0-9_]`.

Expected Outcome
The app rejects the input, shows the username validation error, logs `VALIDATION_FAILURE`, and does not create the account.

---

## 6
Control Group
Data Validation

Test Scenario
Attempt registration with a weak password that violates the policy (e.g. missing uppercase, lowercase, digit, or special character, or length < 8).

Expected Outcome
The app rejects the password, shows the password policy error message, logs `VALIDATION_FAILURE`, and does not create the account.

---

## 7
Control Group
Data Validation

Test Scenario
Attempt registration with valid username and password but mismatched confirmation password.

Expected Outcome
The app rejects the registration with "Passwords do not match.", logs `VALIDATION_FAILURE`, and does not create the account.

---

## 8
Control Group
Data Validation

Test Scenario
Complete registration with a valid username, valid password, and matching confirmation.

Expected Outcome
The app successfully creates the account, returns to the login screen, and the new user can log in.

---

## 9
Control Group
Authorization / Error Handling

Test Scenario
As a client, choose a product and enter invalid purchase quantities: `0`, `-1`, or a value greater than available stock.

Expected Outcome
The app shows a validation error or purchase failure message, does not update inventory, and does not create a purchase history entry.

---

## 10
Control Group
Authorization

Test Scenario
As staff or manager, use the product management actions: add a new product, edit an existing product, and delete a product. Then try the same actions as a client.

Expected Outcome
- Staff/manager may manage products successfully.
- Clients receive "Access Denied" when attempting product management actions.

---

## 11
Control Group
Logging / Authorization

Test Scenario
Log in as `admin`, open the Logs screen, and confirm that the `CLEAR` and `ENABLE/DISABLE DEBUG MODE` buttons are visible and functional.

Expected Outcome
Admin sees logs and the debug controls. Clearing logs refreshes the table, and toggling debug mode changes the internal debug state.

---

## 12
Control Group
Error Handling

Test Scenario
Trigger an unexpected runtime input error in a form (for example, enter non-numeric text where a number is required while adding or purchasing a product).

Expected Outcome
The app shows the generic error message "An error occurred. Please try again." and recovers gracefully without crashing.
