package com.prestashop.test.tests2;

import com.microsoft.playwright.Page;
import com.prestashop.test.base.BaseTest;
import com.prestashop.test.pages.CartPage;
import com.prestashop.test.pages.CheckoutPage;
import com.prestashop.test.pages.HomePage;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.fail;

@Disabled
@Tag("regression")
@DisplayName("PrestaShop storefront - Checkout")
@SuppressWarnings("checkstyle:MagicNumber")
class RunnerTest1 extends BaseTest {

    @Test
    @DisplayName("RUN-02 - runner2")
    void runner2(Page page) {
        CartPage cart = new HomePage(page, "en").open()
                .openCategory("Clothes")
                .openProductByName("Hummingbird printed t-shirt")
                .addToCart()
                .proceedToCart();

//        fail("Failing test to check retry mechanism");
        Random r = new Random();
        int i = r.nextInt(2);
        if (i == 0) {
            fail("Failing test to check retry mechanism");
        }

        CheckoutPage checkout = cart
                .proceedToCheckout()
                .checkStructure()
                .checkPersonalInfoStructure()
                .fillPersonalInfo("Jane", "Doe", "jane.doe.chk01@example.com")
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
                .verifyProductPresent("Hummingbird printed t-shirt")
                .verifySubtotal(22.94)
                .verifyShipping(8.40).verifyTotal(31.34)
                .verifyShippingMethod("My carrier")
                .verifyPaymentMethod("Cash on delivery (COD)");
    }


    @Test
    @DisplayName("RUN-03 - runner3")
    void runner3(Page page) {
        CartPage cart = new HomePage(page, "en").open()
                .openCategory("Clothes")
                .openProductByName("Hummingbird printed t-shirt")
                .addToCart()
                .proceedToCart();

//        fail("Failing test to check retry mechanism");
        Random r = new Random();
        int i = r.nextInt(3);
        if (i == 0) {
            fail("Failing test to check retry mechanism");
        }

        CheckoutPage checkout = cart
                .proceedToCheckout()
                .checkStructure()
                .checkPersonalInfoStructure()
                .fillPersonalInfo("Jane", "Doe", "jane.doe.chk01@example.com")
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
                .verifyProductPresent("Hummingbird printed t-shirt")
                .verifySubtotal(22.94)
                .verifyShipping(8.40).verifyTotal(31.34)
                .verifyShippingMethod("My carrier")
                .verifyPaymentMethod("Cash on delivery (COD)");
    }

}
