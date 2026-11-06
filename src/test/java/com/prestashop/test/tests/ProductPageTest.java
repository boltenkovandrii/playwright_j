package com.prestashop.test.tests;

import com.microsoft.playwright.Page;
import com.prestashop.test.base.TestBase;
import com.prestashop.test.base2.CustomRunnerTest;
import com.prestashop.test.pages.HomePage;
import com.prestashop.test.pages.ProductPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("regression")
@DisplayName("PrestaShop storefront - Product details")
class ProductPageTest extends TestBase {
    private static final String T_SHIRT = "Hummingbird printed t-shirt";

    @CustomRunnerTest
    @DisplayName("PDP-01 - Check product page structure")
    void productPageStructure(Page page) {
        new HomePage(page, "en")
                .open()
                .openCategory("Clothes")
                .openProduct(0)
                .checkStructure();
    }

    @CustomRunnerTest
    @DisplayName("PDP-02 - View product information")
    void productInformation(Page page) {
        new HomePage(page, "en")
                .open()
                .openCategory("Clothes")
                .openProductByName("Hummingbird printed sweater")
                .verifyProductName("Hummingbird printed sweater")
                .verifyRegularPrice(43.08)
                .verifyPrice(34.46)
                .verifyShortDescription("Regular fit, round neckline, long sleeves. 100% cotton, brushed inner side for extra comfort.")
                .verifyInStockCount(1200)
                .verifyFullDescription("Studio Design' PolyFaune collection features classic products with colorful patterns, "
                                + "inspired by the traditional japanese origamis. To wear with a chino or jeans. "
                                + "The sublimation textile printing process provides an exceptional color rendering and a color, "
                                + "guaranteed overtime.");
    }

    @CustomRunnerTest
    @DisplayName("PDP-03 - Change product quantity")
    void changeProductQuantity(Page page) {
        ProductPage product = new HomePage(page, "en")
                .open()
                .openCategory("Clothes")
                .openProductByName(T_SHIRT);

        product.verifyQuantity(1) //Check default quantity
                .increaseQuantity() //Increase the quantity to 2 using arrows.
                .verifyQuantity(2)
                .decreaseQuantity() //Decrease the quantity back to 1 using arrows.
                .verifyQuantity(1)
                .decreaseQuantity() //Try to decrease the quantity below 1 using arrows, the quantity should remain 1.
                .verifyQuantity(1)
                .setQuantity(0) //Try to set the quantity to 0 using the input field, the quantity should remain 1.
                .verifyQuantity(1)
                .setQuantity(3) //Set the quantity to 3 using the input field.
                .verifyQuantity(3);
    }

    @CustomRunnerTest
    @DisplayName("PDP-04 - Select product combination")
    void selectProductCombination(Page page) {
        ProductPage product = new HomePage(page, "en").open().openCategory("Clothes").openProductByName(T_SHIRT);
        product.verifySizeOptions("S", "M", "L", "XL")
                .verifyColorOptionCount(2)
                .verifySelectedSize("S")
                .verifySelectedColor("White")
                .selectSize("M")
                .verifySelectedSize("M")
                .verifyProductContextVisible();

        String original = product.imageSource();
        product.selectColor("Black")
                .verifySelectedColor("Black")
                .verifyProductContextVisible();
        assertThat(product.imageSource()).isNotEqualTo(original);
    }

    @CustomRunnerTest
    @DisplayName("PDP-05 - Add product to cart")
    void addProductToCart(Page page) {
        new HomePage(page, "en")
                .open()
                .openCategory("Clothes")
                .openProductByName(T_SHIRT)
                .setQuantity(2)
                .addToCart()
                .verifyAddToCartConfirmation(T_SHIRT, 2, 45.89, 45.89)
                .verifyHeaderCartCount(2);
    }

    @CustomRunnerTest
    @DisplayName("PDP-06 - Continue shopping after adding a product")
    void continueShoppingAfterAdd(Page page) {
        ProductPage product = new HomePage(page, "en")
                .open()
                .openCategory("Clothes")
                .openProductByName(T_SHIRT);

        product.addToCart()
                .verifyAddToCartConfirmation(T_SHIRT, 1, 22.94, 22.94)
                .continueShopping()
                .verifyAddToCartConfirmationClosed()
                .verifyProductName(T_SHIRT)
                .verifyProductContextVisible()
                .verifyHeaderCartCount(1);

        product.addToCart()
                .verifyAddToCartConfirmation(T_SHIRT, 2, 45.89, 45.89)
                .continueShopping()
                .verifyAddToCartConfirmationClosed()
                .verifyProductName(T_SHIRT)
                .verifyProductContextVisible()
                .verifyHeaderCartCount(2);
    }

    @CustomRunnerTest
    @DisplayName("PDP-07 - Navigate to cart from confirmation dialog")
    void navigateToCartFromConfirmation(Page page) {
        new HomePage(page, "en")
                .open()
                .openCategory("Clothes")
                .openProductByName(T_SHIRT)
                .addToCart()
                .verifyAddToCartConfirmation(T_SHIRT, 1, 22.94, 22.94)
                .proceedToCart()
                .verifyProductCount(1)
                .verifyProductWithNamePresent(T_SHIRT, 1)
                .verifyProductPrice(T_SHIRT, 22.94)
                .verifyProductsSubtotal(22.94)
                .verifyTotal(22.94);
    }
}
