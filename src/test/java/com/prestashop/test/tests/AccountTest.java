package com.prestashop.test.tests;

import com.prestashop.test.base.TestBase;
import com.prestashop.test.pages.AccountDashboardPage;
import com.prestashop.test.pages.HomePage;
import com.prestashop.test.pages.LoginPage;
import com.prestashop.test.pages.RegistrationPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.UUID;

@Tag("regression")
@DisplayName("PrestaShop storefront - Account and guest checkout")
class AccountTest extends TestBase {

    private static final String DEMO_EMAIL = "pub@prestashop.com";
    private static final String DEMO_PASSWORD = "123456789";

    @Test
    @DisplayName("ACCT-01 - Login page structure")
    void loginPageStructures() {
        runScenario(page -> {
            new HomePage(page, "en")
                    .open().openLoginPage()
                    .checkStructure();
        });
    }

    @Test
    @DisplayName("ACCT-02 - Registration page structure")
    void registrationPageStructures() {
        runScenario(page -> {
            new HomePage(page, "en")
                    .open().openLoginPage()
                    .openRegistrationPage()
                    .checkStructure();
        });
    }

    @Test
    @DisplayName("ACCT-03 - Login validation")
    void loginValidation() {
        runScenario(page -> {
            LoginPage login = new HomePage(page, "en").open().openLoginPage();
            // login with empty credentials
            login.loginWithInvalid("", "").verifyEmailError();
            // login with invalid credentials
            login.loginWithInvalid("nonexistent.user@example.com", "password").verifyErrorMessage("Authentication failed.");
            // login with valid credentials
            login.login(DEMO_EMAIL, DEMO_PASSWORD).openAccountPage().verifyLoggedIn();
        });
    }

    @Test
    @DisplayName("ACCT-04 - Registration validation")
    void registrationValidation() {
        runScenario(page -> {
            RegistrationPage registration = new HomePage(page, "en")
                    .open()
                    .openLoginPage()
                    .openRegistrationPage();

            registration = registration.submit()
                    .asRegistrationPage()
                    .verifyFirstNameError();

            registration = registration.fillWith("A", "B", "not-an-email", "", "")
                    .submit()
                    .asRegistrationPage()
                    .verifyEmailError();

            String uniqueEmail = "testuser." + UUID.randomUUID().toString().substring(0, 8) + "@example.com";
            registration = registration.fillWith("A", "B", uniqueEmail, "1", "")
                    .submit()
                    .asRegistrationPage()
                    .verifyPasswordError();

            registration = registration.fillWith("Jane", "Doe", DEMO_EMAIL, "ValidPass123!", "01/01/1990")
                    .submit()
                    .asRegistrationPage()
                    .verifyErrorMessage("The email is already used, please choose another one or sign in");

            registration.fillWith("Jane", "Doe", uniqueEmail, "ValidPass123!", "01/01/1990")
                    .submit()
                    .asHomePage()
                    .openAccountPage()
                    .verifyLoggedIn();
        });
    }

    @Test
    @DisplayName("ACCT-05 - Logout")
    void loginAndLogout() {
        runScenario(page -> {
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

        });
    }

}
