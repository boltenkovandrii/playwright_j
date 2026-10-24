### ACCT-01 — Login page structure

**Purpose**

Verify that the login page opens correctly and all key UI elements are present.

**Preconditions**

- Storefront is available.
- The user is not logged in.

**Scenario**

1. Open the storefront home page.
2. Navigate to the login page via the header menu.
3. Inspect the login page structure.

**Expected result**

- The login page is displayed.
- The page heading (e.g., "Login") is visible.
- The email input field is present.
- The password input field is present.
- The "Sign in" submit button is visible.
- A link to the registration page is present.
- The header and footer are present.

**Coverage**

- Login page layout
- Core authentication UI elements
- Navigation to the login page

**Notes**

- Profile dimension is relevant: the header layout may differ between desktop and mobile viewports.

---

### ACCT-02 — Registration page structure

**Purpose**

Verify that the account registration page opens correctly and all key UI elements are present.

**Preconditions**

- Storefront is available.
- The user is not logged in.

**Scenario**

1. Open the storefront home page.
2. Navigate to the login page via the header menu.
3. Navigate to the registration page.
4. Inspect the registration page structure.

**Expected result**

- The registration page is displayed.
- The page heading "Create an account" is visible.
- Input fields for first name, last name, email, password and birthday are present as well as social title radio buttons and checkboxes for offers\privacy policy\newsletter\data privacy.
- A submit button to create the account is visible.
- The header and footer are present.

**Coverage**

- Registration page layout
- Core registration form UI elements
- Navigation to the registration page

**Notes**

- The exact set of registration fields may depend on the PrestaShop configuration and theme.

---

### ACCT-03 — Login validation

**Purpose**

Verify that the login form correctly handles invalid credentials and allows a successful login with valid credentials.

**Preconditions**

- Storefront is available.
- The user is not logged in.
- A valid demo account exists: `pub@prestashop.com` / `123456789`.

**Scenario**

1. Open the login page.
2. Submit the form with an empty email and empty password.
3. Observe validation feedback.
4. Enter a correctly formatted but non-existent email address and any password, then submit.
5. Observe the error message.
6. Enter the correct email (`pub@prestashop.com`) with an incorrect password, then submit.
7. Observe the error message.
8. Enter the correct credentials (`pub@prestashop.com` / `123456789`) and submit.
9. Observe the resulting page.
10. Attempt to navigate to an authenticated page (e.g., account dashboard) and confirm access.
11. Check that the header or account menu reflects the logged-in state (e.g., customer name is visible).

**Expected result**

- Submitting with empty fields shows required-field validation messages (inline or summary).
- Using a non-existent email shows an authentication error.
- Using the correct email with a wrong password shows an authentication error.
- Using valid credentials successfully logs in and redirects to the home page.
- User can navigate to the authenticated pages - i.e. account dashboard.
- After login, the account menu or header reflects the logged-in state (e.g., customer name is visible).

**Coverage**

- Login form validation (empty fields)
- Authentication error handling (wrong credentials)
- Successful login flow
- Post-login page state

**Notes**

- The exact wording of validation and error messages may vary by storefront theme and locale.
- This scenario depends on the demo user account remaining available; shared demo environments may be affected by other test runs (e.g., a locked account after repeated failures).

---

### ACCT-04 — Registration validation

**Purpose**

Verify that the registration form correctly validates required fields and input constraints, and prevents submission of invalid data.

**Preconditions**

- Storefront is available.
- The user is not logged in.

**Scenario**

1. Open the registration page.
2. Submit the form with all fields empty.
3. Observe validation feedback.
4. Enter an invalid email format then submit.
5. Observe the validation messages.
6. Enter valid email, but a password that does not meet the minimum requirements (e.g., too short), and submit.
7. Observe the validation messages.
8. Enter an email address that is already registered (e.g., `pub@prestashop.com`), fill in remaining fields with valid data, and submit.
9. Observe the error message.
10. Fill in all fields with valid, unique data and submit.
11. Observe the resulting page.
12. Try to navigate to the authenticated pages - i.e. account dashboard.

**Expected result**

- Submitting with empty fields shows required-field validation messages for each mandatory field.
- An invalid email format triggers an email validation error.
- A password that does not meet the minimum requirements triggers a password validation error.
- Using an already-registered email address shows an error indicating the account already exists.
- Submitting valid unique data creates the account and redirects to the home page.
- User can navigate to the authenticated pages - i.e. account dashboard.

**Coverage**

- Registration form validation (empty fields, invalid format)
- Duplicate email detection
- Password complexity validation
- Successful account creation flow

**Notes**

- Successful registration creates a real account. To keep the environment clean, consider using a disposable or randomised email pattern during implementation.
- The exact password complexity rules depend on the PrestaShop shop configuration.
- This scenario is potentially brittle if run repeatedly with the same test data; uniqueness of email must be managed per test run.

---

### ACCT-05 — Logout

**Purpose**

Verify that a logged-in user can successfully sign out and is returned to an unauthenticated state.

**Preconditions**

- Storefront is available.
- The user is logged in with the demo account: `pub@prestashop.com` / `123456789`.

**Scenario**

1. Log in with the demo credentials.
2. Confirm the logged-in state is visible.
3. Locate and click the "Sign out" link.
4. Observe the resulting page and header state.
5. Attempt to navigate directly to the account dashboard URL.
6. Observe whether access is restricted.

**Expected result**

- After clicking "Sign out", the user is logged out
- The header no longer shows the logged-in customer name.
- The sign-in link is visible again in the header or account menu.
- Navigating directly to the account dashboard URL redirects the user to the login page (access is protected).

**Coverage**

- Logout flow
- Post-logout page state and header update
- Protected page access after logout

**Notes**

- The redirect target after logout may be the home page or login page depending on the PrestaShop configuration.
- Step 5–6 (accessing a protected page after logout) is a useful security boundary check and a good demonstration of Playwright's navigation and redirect assertion capabilities.

