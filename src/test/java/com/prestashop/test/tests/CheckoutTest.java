package com.prestashop.test.tests;

import com.prestashop.test.base.TestBase;
import com.prestashop.test.pages.CartPage;
import com.prestashop.test.pages.CheckoutPage;
import com.prestashop.test.pages.HomePage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("regression")
@DisplayName("PrestaShop storefront - Checkout")
class CheckoutTest extends TestBase {


    //TODO: add missing verification steps

    @Test
    @DisplayName("CHK-01 — Guest checkout page structure and checkout flow")
    void guestCheckout() {
        runScenario(page -> {
            CartPage cart = new HomePage(page, "en").open()
                    .openCategory("Clothes")
                    .openProductByName("Hummingbird printed t-shirt")
                    .addToCart()
                    .proceedToCart();
            CheckoutPage checkout = cart
                    .proceedToCheckout()
                    .checkStructure()
                    .fillPersonalInfo("Jane", "Doe", "jane.doe.chk01@example.com")
                    .fillAddress("221B Baker Street", "Paris", "75001", "France", "5551234567", null)
                    .selectShippingMethod("My carrier")
                    .selectPaymentMethod("Pay by Cash on Delivery")
                    .acceptTerms();
            checkout.placeOrder().checkStructure().verifyOrderReferencePresent()
                    .verifyProductPresent("Hummingbird printed t-shirt").verifySubtotal(22.94)
                    .verifyShipping(8.40).verifyTotal(31.34).verifyShippingMethod("My carrier")
                    .verifyPaymentMethod("Cash on delivery (COD)");
        });
    }
}
