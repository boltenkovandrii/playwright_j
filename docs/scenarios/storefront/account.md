### ACCT-01 - Login page structure

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

---

### ACCT-02 - Registration page structure

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
- Input fields for first name, last name, email, password and birthdate are present as well as social title radio buttons and checkboxes for offers\privacy policy\newsletter\data privacy.
- A submit button to create the account is visible.
- The header and footer are present.

---

### ACCT-03 - Login validation

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

**Notes**

- The exact wording of validation and error messages may vary by storefront theme and locale.
- This scenario depends on the demo user account remaining available; shared demo environments may be affected by other test runs (e.g., a locked account after repeated failures).

---

### ACCT-04 - Registration validation

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
10. Enter a first name containing a disallowed character (e.g., a digit such as `Qa1`), enter valid password and submit.
11. Observe the field-level validation message.
12. Correct the first name and enter a last name containing a disallowed character (e.g., a digit such as `Qa1`), enter valid password and submit.
13. Observe the field-level validation message.
14. Correct the last name, enter an invalid birthdate (e.g., `31/12/1990`), re-enter the password and submit.
15. Observe the field-level validation message.
16. Fill in all fields with valid, unique data, check mandatory consent checkboxes and submit.
17. Observe the resulting page.
18. Try to navigate to the authenticated pages - i.e. account dashboard.

**Expected result**

- Submitting with empty fields shows required-field validation messages for at least one mandatory field which is missing.
- An invalid email format triggers an email validation error.
- A password that does not meet the minimum requirements triggers a password validation error.
- Using an already-registered email address shows an error indicating the account already exists.
- A first name containing a disallowed character is rejected with a field-level validation error (e.g., "Invalid format.") and no account is created.
- An invalid birthdate is rejected with a field-level validation error (e.g., "Format should be 05/31/1970.") and no account is created.
- Submitting valid unique data creates the account and redirects to the home page.
- User can navigate to the authenticated pages - i.e. account dashboard.

**Notes**

- Validation errors may be rendered inline next to the affected field and/or in a page-level summary. Assert that a clear validation error is present; do not depend on a specific message wording or element count.
- The password field is cleared after each failed save attempt, so re-enter it before every resubmission.
- The name fields accept only letters, spaces and dots; use a clearly disallowed value such as `Qa1` rather than a borderline case.
- Password requirements are 8-72 characters with the configured complexity rules. Do not test every rule combination; verify only that a non-matching password is rejected.
- Both consent checkboxes are mandatory in this environment.

---

### ACCT-05 - Logout

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

---

### ACCT-06 - Registration keeps the shopping cart

**Purpose**

Verify that a guest with items in the shopping cart can create an account without losing the cart contents.

**Preconditions**

- Storefront is available.
- The user is not logged in.
- The shopping cart contains at least one product.

**Scenario**

1. Add a product to the cart and confirm the cart contains one item.
2. Open the registration page.
3. Fill in all fields with valid, unique data and check the mandatory consent checkboxes.
4. Submit the form.
5. Observe the header and the resulting page.
6. Open the cart page.

**Expected result**

- The account is created and the user is signed in; the header shows the customer name and the "Sign out" link.
- The cart still contains the product added before registration.
- The preserved cart can be used to continue to checkout in the signed-in session.

**Notes**

- Registration does not require re-adding products to the cart.

---

### ACCT-07 - Account creation during checkout

**Purpose**

Verify that a guest can create an account while placing an order and can use the new credentials afterwards.

**Preconditions**

- Storefront is available.
- The user is not logged in.
- The shopping cart contains at least one product.

**Scenario**

1. Open the cart and proceed to checkout.
2. On the "Personal Information" step, keep the "Order as a guest" tab selected.
3. Enter valid, unique personal data and a password in the optional account creation section.
4. Accept the consent checkboxes and continue.
5. Complete the address, shipping and payment steps and place the order.
6. Observe the order confirmation page.
7. Sign out.
8. Sign in with the email and password entered in step 3.
9. Open the order history.

**Expected result**

- The "Personal Information" step offers optional account creation with a password field.
- The order is placed and the order confirmation page is displayed.
- Signing in with the credentials entered during checkout succeeds.
- The order history shows the order placed during checkout.

**Notes**

- Account creation at checkout is optional; leaving the password empty keeps the order as a guest order.
- Placing orders is acceptable in the local Docker environment.

---

### ACCT-08 - Registration requires both consent checkboxes

**Purpose**

Verify that the registration form is not submitted unless both consent checkboxes are selected.

**Preconditions**

- Storefront is available.
- The user is not logged in.

**Scenario**

1. Open the registration page.
2. Fill in all fields with valid, unique data.
3. Submit the form with both consent checkboxes unchecked.
4. Observe the page state.
5. Check only "I agree to the terms and conditions and the privacy policy" and submit.
6. Observe the page state.
7. Check only "Customer data privacy" and submit.
8. Observe the page state.
9. Check both consent checkboxes and submit.
10. Observe the resulting page.

**Expected result**

- With no consent checkbox selected, the form is not submitted and no account is created.
- With only the terms and conditions checkbox selected, the form is still not submitted and no account is created.
- With both checkboxes selected, the account is created and the user is signed in.

**Notes**

- Both consent checkboxes are mandatory in this environment.
- In the inspected browser, a missing consent checkbox blocks submission without a separate error message and the form stays on the registration page. Do not depend on a specific message text.

---

### ACCT-09 - Registration is unavailable to a signed-in customer

**Purpose**

Verify that an authenticated customer cannot open the registration page.

**Preconditions**

- Storefront is available.
- A valid demo account exists: `pub@prestashop.com` / `123456789`.

**Scenario**

1. Sign in with valid credentials.
2. Confirm the signed-in state in the header.
3. Navigate directly to the registration page URL.
4. Observe the resulting page and URL.

**Expected result**

- The user is redirected from the registration page to the account dashboard.
- The account dashboard is displayed and the header still shows the signed-in customer.
- The registration form is not shown to the signed-in customer.


