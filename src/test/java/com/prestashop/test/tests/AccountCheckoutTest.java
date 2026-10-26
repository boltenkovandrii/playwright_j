package com.prestashop.test.tests;

import com.prestashop.test.base.TestBase;
import com.prestashop.test.pages.HomePage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("regression")
@DisplayName("PrestaShop storefront - Account and guest checkout")
class AccountCheckoutTest extends TestBase {

    private static final String DEMO_EMAIL = "pub@prestashop.com";
    private static final String DEMO_PASSWORD = "123456789";

    @Test
    @DisplayName("ACCT-01/02 — Login and registration page structures")
    void accountPageStructures() {
        runScenario(page -> {
            var login = new HomePage(page, "en").open().openLoginPage().checkStructure();
            login.openRegistrationPage().checkStructure();
        });
    }

    @Test
    @DisplayName("ACCT-03/05 — Login validation and logout")
    void loginAndLogout() {
        runScenario(page -> {
            var login = new HomePage(page, "en").open().openLoginPage();
            login.loginWithInvalid("", "").verifyEmailError();
            login.loginWithInvalid("nonexistent.user@example.com", "password")
                    .verifyErrorMessage("Authentication failed.");
            var account = login.login(DEMO_EMAIL, DEMO_PASSWORD).openAccountPage().verifyLoggedIn();
            account.signOut().asLoginPage().verifyLoaded();
        });
    }

    @Test
    @DisplayName("CHK-01 — Guest checkout flow")
    void guestCheckout() {
        runScenario(page -> {
            var cart = new HomePage(page, "en").open().openCategory("Clothes")
                    .openProductByName("Hummingbird printed t-shirt").addToCart().proceedToCart();
            var checkout = cart.proceedToCheckout().checkStructure()
                    .fillPersonalInfo("Jane", "Doe", "jane.doe.chk01@example.com")
                    .fillAddress("221B Baker Street", "Paris", "75001", "France", "5551234567", null)
                    .selectShippingMethod("My carrier").selectPaymentMethod("Pay by Cash on Delivery").acceptTerms();
            checkout.placeOrder().checkStructure().verifyOrderReferencePresent()
                    .verifyProductPresent("Hummingbird printed t-shirt").verifySubtotal(22.94)
                    .verifyShipping(8.40).verifyTotal(31.34).verifyShippingMethod("My carrier")
                    .verifyPaymentMethod("Cash on delivery (COD)");
        });
    }
}
