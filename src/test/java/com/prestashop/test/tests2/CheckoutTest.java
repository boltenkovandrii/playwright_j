package com.prestashop.test.tests2;

import com.microsoft.playwright.Page;
import com.prestashop.test.base.TestRunConfig;
import com.prestashop.test.base2.BrowserMatrixTest;
import com.prestashop.test.pages.CartPage;
import com.prestashop.test.pages.CheckoutPage;
import com.prestashop.test.pages.HomePage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.fail;


@Tag("regression")
@DisplayName("PrestaShop storefront - Checkout")
class CheckoutTest  {

    @BrowserMatrixTest
    @DisplayName("CHK-01 - Guest checkout page structure and checkout flow")
    void guestCheckout(TestRunConfig run, Page page) {
        CartPage cart = new HomePage(page, "en").open()
                .openCategory("Clothes")
                .openProductByName("Hummingbird printed t-shirt")
                .addToCart()
                .proceedToCart();

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


        Random random = new Random();
        int i = random.nextInt(2);
        if (i == 0) {
            fail("Failing test to check retry mechanism: " + i);
        }

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
