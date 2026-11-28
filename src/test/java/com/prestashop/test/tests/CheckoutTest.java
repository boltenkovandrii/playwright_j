package com.prestashop.test.tests;

import com.microsoft.playwright.Page;
import com.prestashop.test.base.BaseTest;
import com.prestashop.test.pages.CartPage;
import com.prestashop.test.pages.CheckoutPage;
import com.prestashop.test.pages.HomePage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.prestashop.test.data.TestData.PRODUCT_T_SHIRT;

@Tag("regression")
@DisplayName("PrestaShop storefront - Checkout")
class CheckoutTest extends BaseTest {

    @Test
    @DisplayName("CHK-01 - Guest checkout page structure and checkout flow.")
    @SuppressWarnings("checkstyle:MagicNumber")
    void guestCheckout(Page page) {
        CartPage cart = new HomePage(page, "en").open()
                .openCategory("Clothes")
                .openProductByName(PRODUCT_T_SHIRT)
                .addToCart()
                .proceedToCart();

        CheckoutPage checkout = cart
                .proceedToCheckout()
                .checkStructure()
                .checkPersonalInfoStructure()
                .fillPersonalInfo("Jane", "Doe", "jane.doe.chk01@example.com", null)
                .checkAddressesStructure()
                .fillAddress("221B Baker Street", "Paris", "75001", "France", "5551234567", null)
                .checkShippingMethodStructure()
                .selectShippingMethod("My carrier")
                .checkPaymentStructure()
                .selectPaymentMethod("Pay by Cash on Delivery")
                .acceptTerms();

        checkout.placeOrder()
                .checkStructure()
                .verifyOrderReferencePresent()
                .verifyProductPresent(PRODUCT_T_SHIRT)
                .verifySubtotal(22.94)
                .verifyShipping(8.40).verifyTotal(31.34)
                .verifyShippingMethod("My carrier")
                .verifyPaymentMethod("Cash on delivery (COD)");
    }
}
