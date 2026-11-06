package com.prestashop.test.tests2;

import com.microsoft.playwright.Page;
import com.prestashop.test.base2.CustomRunnerTest;
import com.prestashop.test.pages.CartPage;
import com.prestashop.test.pages.CheckoutPage;
import com.prestashop.test.pages.HomePage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;

import static com.prestashop.test.tests2.StaticTestManager.chk2;
import static com.prestashop.test.tests2.StaticTestManager.chk3;
import static org.junit.jupiter.api.Assertions.fail;


@Tag("regression")
@DisplayName("PrestaShop storefront - Checkout")
class CheckoutTest2 {

    @CustomRunnerTest
    @DisplayName("CHK-02 - Guest checkout page structure and checkout flow")
    void guestCheckout2(Page page) {
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


        chk2++;
        if (chk2 < 2) {
            fail("Failing test to check retry mechanism: " + chk2);
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


    @CustomRunnerTest
    @DisplayName("CHK-03 - Guest checkout page structure and checkout flow")
    void guestCheckout(Page page) {
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


        chk3++;
        if (chk3 <3) {
            fail("Failing test to check retry mechanism: " + chk3);
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
