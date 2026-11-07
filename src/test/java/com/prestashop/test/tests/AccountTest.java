package com.prestashop.test.tests;

import com.microsoft.playwright.Page;
import com.prestashop.test.base2.CustomRunnerTest;
import com.prestashop.test.pages.AccountDashboardPage;
import com.prestashop.test.pages.HomePage;
import com.prestashop.test.pages.LoginPage;
import com.prestashop.test.pages.RegistrationPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;

import java.util.UUID;

@Tag("regression")
@DisplayName("PrestaShop storefront - Account and guest checkout")
class AccountTest {

    private static final String DEMO_EMAIL = "pub@prestashop.com";
    private static final String DEMO_PASSWORD = "123456789";

    @CustomRunnerTest
    @DisplayName("ACCT-01 - Login page structure")
    void loginPageStructures(Page page) {
        new HomePage(page, "en")
                .open()
                .openLoginPage()
                .checkStructure();
    }

    @CustomRunnerTest
    @DisplayName("ACCT-02 - Registration page structure")
    void registrationPageStructures(Page page) {
        new HomePage(page, "en")
                .open().openLoginPage()
                .openRegistrationPage()
                .checkStructure();
    }

    @CustomRunnerTest
    @DisplayName("ACCT-03 - Login validation")
    void loginValidation(Page page) {
        LoginPage login = new HomePage(page, "en").open().openLoginPage();
        // login with empty credentials
        login.loginWithInvalid("", "").verifyEmailError();
        // login with invalid credentials
        login.loginWithInvalid("nonexistent.user@example.com", "password").verifyErrorMessage("Authentication failed.");
        // login with valid credentials
        login.login(DEMO_EMAIL, DEMO_PASSWORD).openAccountPage().verifyLoggedIn();
    }

    @CustomRunnerTest
    @DisplayName("ACCT-04 - Registration validation")
    @SuppressWarnings("checkstyle:MagicNumber")
    void registrationValidation(Page page) {
        RegistrationPage registration = new HomePage(page, "en")
                .open()
                .openLoginPage()
                .openRegistrationPage();

        // Submit the form with all fields empty.
        registration = registration.submit()
                .asRegistrationPage()
                .verifyFirstNameError();

        // Enter an invalid email format then submit.
        registration = registration.fillWith("A", "B", "not-an-email", "", "")
                .submit()
                .asRegistrationPage()
                .verifyEmailError();

        // Enter valid email, but a password that does not meet the minimum requirements (e.g., too short), and submit.
        String uniqueEmail = "testuser." + UUID.randomUUID().toString().substring(0, 8) + "@example.com";
        registration = registration.fillWith("A", "B", uniqueEmail, "1", "")
                .submit()
                .asRegistrationPage()
                .verifyPasswordError();

        // Entering an email address that is already registered
        registration = registration.fillWith("Jane", "Doe", DEMO_EMAIL, "ValidPass123!", "01/01/1990")
                .submit()
                .asRegistrationPage()
                .verifyErrorMessage("The email is already used, please choose another one or sign in");

        // Valid input
        registration.fillWith("Jane", "Doe", uniqueEmail, "ValidPass123!", "01/01/1990")
                .submit()
                .asHomePage()
                .openAccountPage()
                .verifyLoggedIn();
    }

    @CustomRunnerTest
    @DisplayName("ACCT-05 - Logout")
    void loginAndLogout(Page page) {
        LoginPage login = new HomePage(page, "en").open().openLoginPage();
        // login with valid credentials
        AccountDashboardPage account = login
                .login(DEMO_EMAIL, DEMO_PASSWORD)
                .openAccountPage();
        account.verifyLoggedIn();

        // logout
        account.signOut()
                .asLoginPage()
                .verifyLoaded();

        // verify that user is logged out by trying to access account dashboard directly
        account.navigateToAccountDashboardDirectly()
                .asLoginPage()
                .verifyNotLoggedIn();
    }

}
