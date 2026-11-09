package com.prestashop.test.tests;

import com.microsoft.playwright.Page;
import com.prestashop.test.runner.CustomRunnerTest;
import com.prestashop.test.pages.CartPage;
import com.prestashop.test.pages.HomePage;
import com.prestashop.test.pages.ProductPage;
import com.prestashop.test.pages.ProductSpec;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;

@Tag("regression")
@DisplayName("PrestaShop storefront - Shopping cart")
class CartPageTest {
    private static final String T_SHIRT = "Hummingbird printed t-shirt";

    @CustomRunnerTest
    @DisplayName("CART-01 - Check cart page structure")
    void cartPageStructure(Page page) {
        new HomePage(page, "en")
                .open()
                .openCategory("Clothes")
                .openProduct(0)
                .addToCart()
                .proceedToCart()
                .checkStructure();
    }

    @CustomRunnerTest
    @DisplayName("CART-02 - Add single product to cart")
    void addSingleProduct(Page page) {
        new HomePage(page, "en")
                .open()
                .openCategory("Clothes")
                .openProductByName(T_SHIRT)
                .addToCart()
                .proceedToCart()
                .verifyProductCount(1)
                .verifyProductQuantity(new ProductSpec(T_SHIRT), 1);
    }

    @CustomRunnerTest
    @DisplayName("CART-03 - Add multiple quantities from product page")
    void addMultipleQuantities(Page page) {
        ProductPage product = new HomePage(page, "en")
                .open()
                .openCategory("Clothes")
                .openProductByName(T_SHIRT);

        product.verifyHeaderCartCount(0)
                .setQuantity(3)
                .addToCart()
                .proceedToCart()
                .verifyProductCount(1)
                .verifyProductQuantity(new ProductSpec(T_SHIRT), 3);
    }

    @CustomRunnerTest
    @DisplayName("CART-04 - Remove product from cart")
    void removeProduct(Page page) {
        new HomePage(page, "en")
                .open().openCategory("Clothes")
                .openProductByName(T_SHIRT)
                .addToCart()
                .proceedToCart()
                .removeProduct(new ProductSpec(T_SHIRT))
                .checkEmptyStructure();
    }

    @CustomRunnerTest
    @DisplayName("CART-05 - Update product quantity in cart")
    @SuppressWarnings("checkstyle:MagicNumber")
    void updateProductQuantity(Page page) {
        ProductSpec product = new ProductSpec(T_SHIRT);
        new HomePage(page, "en")
                .open()
                .openCategory("Clothes")
                .openProductByName(T_SHIRT)
                .addToCart()
                .proceedToCart()
                .verifyProductQuantity(product, 1)
                .verifyProductsSubtotal(22.94)
                .verifyTotal(22.94)
                .setProductQuantity(product, 2)
                .verifyProductQuantity(product, 2)
                .verifyProductsSubtotal(45.89)
                .verifyTotal(45.89);
    }

    @CustomRunnerTest
    @DisplayName("CART-06 - Continue shopping from cart")
    void continueShopping(Page page) {
        ProductPage product = new HomePage(page, "en")
                .open()
                .openCategory("Clothes")
                .openProductByName(T_SHIRT);
        HomePage home = product.addToCart()
                .proceedToCart()
                .continueShopping();

        home.verifyHeaderCartCount(1);

        home.openCategory("Clothes")
                .openProductByName(T_SHIRT)
                .addToCart()
                .proceedToCart()
                .continueShopping()
                .verifyHeaderCartCount(2);
    }

    @CustomRunnerTest
    @DisplayName("CART-07 - Verify cart totals for multiple variants")
    @SuppressWarnings("checkstyle:MagicNumber")
    void cartTotalsForVariants(Page page) {
        ProductPage product = new HomePage(page, "en")
                .open()
                .openCategory("Clothes")
                .openProductByName(T_SHIRT);
        product.selectSize("M")
                .selectColor("Black")
                .setQuantity(2)
                .addToCart()
                .continueShopping()
                .selectSize("XL")
                .selectColor("White")
                .setQuantity(3);
        CartPage cart = product.addToCart().proceedToCart();
        ProductSpec mediumBlack = new ProductSpec(T_SHIRT, java.util.Map.of("size", "M", "color", "Black"));
        ProductSpec extraLargeWhite = new ProductSpec(T_SHIRT, java.util.Map.of("size", "XL", "color", "White"));
        cart.verifyProductCount(2)
                .verifyProductQuantity(mediumBlack, 2)
                .verifyProductQuantity(extraLargeWhite, 3)
                .verifyProductsSubtotal(114.72)
                .verifyTotal(114.72);
    }
}
