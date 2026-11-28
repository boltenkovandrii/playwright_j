package com.prestashop.test.tests;

import static com.prestashop.test.data.TestData.DEMO_EMAIL;
import static com.prestashop.test.data.TestData.DEMO_PASSWORD;
import static com.prestashop.test.data.TestData.PRODUCT_T_SHIRT;

import com.microsoft.playwright.Page;
import com.prestashop.test.base.BaseTest;
import com.prestashop.test.pages.AccountDashboardPage;
import com.prestashop.test.pages.CartPage;
import com.prestashop.test.pages.CheckoutPage;
import com.prestashop.test.pages.HomePage;
import com.prestashop.test.pages.LoginPage;
import com.prestashop.test.pages.OrderConfirmationPage;
import com.prestashop.test.pages.ProductPage;
import com.prestashop.test.pages.RegistrationPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.UUID;

@Tag("regression")
@DisplayName("PrestaShop storefront - Account and guest checkout")
class AccountTest extends BaseTest {

    @Test
    @DisplayName("ACCT-01 - Login page structure.")
    void loginPageStructures(Page page) {
        new HomePage(page, "en")
                .open()
                .openLoginPage()
                .checkStructure();
    }

    @Test
    @DisplayName("ACCT-02 - Registration page structure.")
    void registrationPageStructures(Page page) {
        new HomePage(page, "en")
                .open().openLoginPage()
                .openRegistrationPage()
                .checkStructure();
    }

    @Test
    @DisplayName("ACCT-03 - Login validation.")
    void loginValidation(Page page) {
        LoginPage login = new HomePage(page, "en").open().openLoginPage();
        // login with empty credentials
        login.loginWithInvalid("", "").verifyEmailError();
        // login with invalid credentials
        login.loginWithInvalid("nonexistent.user@example.com", "password").verifyErrorMessage("Authentication failed.");
        // login with valid credentials
        login.login(DEMO_EMAIL, DEMO_PASSWORD).openAccountPage().verifyLoggedIn();
    }

    @Test
    @DisplayName("ACCT-04 - Registration validation.")
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

        // Enter a first name containing a disallowed character.
        registration = registration.fillWith("Qa1", "Doe", uniqueEmail, "ValidPass123!", "01/01/1990")
                .submit()
                .asRegistrationPage()
                .verifyFirstNameFormatError();

        // Correct the first name and enter a last name containing a disallowed character.
        registration = registration.fillWith("Jane", "Qa1", uniqueEmail, "ValidPass123!", "01/01/1990")
                .submit()
                .asRegistrationPage()
                .verifyLastNameFormatError();

        // Correct the last name, enter an invalid birthdate.
        registration = registration.fillWith("Jane", "Doe", uniqueEmail, "ValidPass123!", "31/12/1990")
                .submit()
                .asRegistrationPage()
                .verifyBirthdateFormatError();

        // Fill in all fields with valid, unique data, check mandatory consent checkboxes and submit.
        registration.fillWith("Jane", "Doe", uniqueEmail, "ValidPass123!", "01/01/1990")
                .submit()
                .asHomePage()
                .openAccountPage()
                .verifyLoggedIn();
    }

    @Test
    @DisplayName("ACCT-05 - Logout.")
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

    @Test
    @DisplayName("ACCT-06 - Registration keeps the shopping cart.")
    @SuppressWarnings("checkstyle:MagicNumber")
    void registrationKeepsShoppingCart(Page page) {
        String uniqueEmail = "qa_scenario_acct06_" + UUID.randomUUID().toString().substring(0, 8) + "@example.com";

        ProductPage product = new HomePage(page, "en")
                .open()
                .openCategory("Clothes")
                .openProductByName(PRODUCT_T_SHIRT)
                .selectSize("M");

        product.addToCart()
                .proceedToCart()
                .verifyProductCount(1)
                .verifyProductWithNamePresent(PRODUCT_T_SHIRT, 1);

        RegistrationPage registration = new HomePage(page, "en")
                .open()
                .openLoginPage()
                .openRegistrationPage();

        HomePage home = registration.fillWith("Jane", "Doe", uniqueEmail, "ValidPass123!", "01/01/1990")
                .submit()
                .asHomePage();

        home.verifyHeaderCartCount(1)
                .verifyLoggedIn();

        home.openCart()
                .verifyProductCount(1)
                .verifyProductWithNamePresent(PRODUCT_T_SHIRT, 1)
                .verifyProceedToCheckoutVisible();
    }

    @Test
    @DisplayName("ACCT-07 - Account creation during checkout.")
    @SuppressWarnings("checkstyle:MagicNumber")
    void accountCreationDuringCheckout(Page page) {
        String uniqueEmail = "qa_scenario_acct07_" + UUID.randomUUID().toString().substring(0, 8) + "@example.com";
        String password = "ValidPass123!";

        CartPage cart = new HomePage(page, "en")
                .open()
                .openCategory("Clothes")
                .openProductByName(PRODUCT_T_SHIRT)
                .selectSize("M")
                .addToCart()
                .proceedToCart();

        CheckoutPage checkout = cart
                .proceedToCheckout()
                .checkStructure()
                .checkPersonalInfoStructure()
                .fillPersonalInfo("Jane", "Doe", uniqueEmail, password)
                .checkAddressesStructure()
                .fillAddress("221B Baker Street", "Paris", "75001", "France", "5551234567", null)
                .checkShippingMethodStructure()
                .selectShippingMethod("My carrier")
                .checkPaymentStructure()
                .selectPaymentMethod("Pay by Cash on Delivery")
                .acceptTerms();

        OrderConfirmationPage confirmation = checkout.placeOrder()
                .checkStructure()
                .verifyOrderReferencePresent();
        String orderReference = confirmation.getOrderReference();

        AccountDashboardPage account = confirmation
                .signOut()
                .openLoginPage()
                .verifyNotLoggedIn()
                .asLoginPage()
                .login(uniqueEmail, password)
                .openAccountPage();
//        page.navigate(BasePage.baseUrl() + "/en/?mylogout=");
//        new HomePage(page, "en").verifyLoaded();

//        LoginPage login = new HomePage(page, "en").open().openLoginPage();
//        AccountDashboardPage account = login.login(uniqueEmail, password).openAccountPage();
//        account.verifyLoggedIn();

        account.navigateToOrderHistoryDirectly()
                .asOrderHistoryPage()
                .verifyOrderReferencePresent(orderReference);
    }

    @Test
    @DisplayName("ACCT-08 - Registration requires both consent checkboxes.")
    @SuppressWarnings("checkstyle:MagicNumber")
    void registrationRequiresBothConsentCheckboxes(Page page) {
        String uniqueEmail = "qa_scenario_acct08_" + UUID.randomUUID().toString().substring(0, 8) + "@example.com";

        RegistrationPage registration = new HomePage(page, "en")
                .open()
                .openLoginPage()
                .openRegistrationPage();

        // No consent checkbox selected: form is not submitted.
        registration = registration.fillWith("Jane", "Doe", uniqueEmail, "ValidPass123!", "01/01/1990", false, false)
                .submit()
                .asRegistrationPage();

        // Only terms and conditions checkbox selected: form is still not submitted.
        registration = registration.fillWith("Jane", "Doe", uniqueEmail, "ValidPass123!", "01/01/1990", false, true)
                .submit()
                .asRegistrationPage();

        // Both checkboxes selected: account is created and user is signed in.
        registration.fillWith("Jane", "Doe", uniqueEmail, "ValidPass123!", "01/01/1990", true, true)
                .submit()
                .asHomePage()
                .verifyLoggedIn();
    }

    @Test
    @DisplayName("ACCT-09 - Registration is unavailable to a signed-in customer.")
    void registrationUnavailableToSignedInCustomer(Page page) {
        LoginPage login = new HomePage(page, "en").open().openLoginPage();
        login.login(DEMO_EMAIL, DEMO_PASSWORD).openAccountPage().verifyLoggedIn();

        login.navigateToRegistrationDirectly()
                .asAccountDashboardPage()
                .verifyLoaded()
                .verifyLoggedIn();

    }

}
