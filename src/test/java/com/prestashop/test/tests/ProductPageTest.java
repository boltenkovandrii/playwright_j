package com.prestashop.test.tests;

import com.prestashop.test.base.TestBase;
import com.prestashop.test.pages.HomePage;
import com.prestashop.test.pages.ProductPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("regression")
@DisplayName("PrestaShop storefront - Product details")
class ProductPageTest extends TestBase {
    private static final String T_SHIRT = "Hummingbird printed t-shirt";

    @Test @DisplayName("PDP-01 - Check product page structure")
    void productPageStructure() {
        runScenario(page -> new HomePage(page, "en")
                .open()
                .openCategory("Clothes")
                .openProduct(0)
                .checkStructure());
    }

    @Test @DisplayName("PDP-02 - View product information")
    void productInformation() {
        runScenario(page -> new HomePage(page, "en")
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
                                + "guaranteed overtime."));
    }

    @Test @DisplayName("PDP-03 - Change product quantity")
    void changeProductQuantity() {
        runScenario(page -> {
            ProductPage product = new HomePage(page, "en")
                    .open()
                    .openCategory("Clothes")
                    .openProductByName(T_SHIRT);

            product.verifyQuantity(1)
                    .increaseQuantity()
                    .verifyQuantity(2)
                    .decreaseQuantity()
                    .verifyQuantity(1)
                    .decreaseQuantity()
                    .verifyQuantity(1)
                    .setQuantity(0)
                    .verifyQuantity(1)
                    .setQuantity(3)
                    .verifyQuantity(3);
        });
    }

    @Test @DisplayName("PDP-04 - Select product combination")
    void selectProductCombination() {
        runScenario(page -> {
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
        });
    }

    @Test @DisplayName("PDP-05 - Add product to cart")
    void addProductToCart() {
        runScenario(page -> new HomePage(page, "en")
                .open()
                .openCategory("Clothes")
                .openProductByName(T_SHIRT)
                .setQuantity(2)
                .addToCart()
                .verifyAddToCartConfirmation(T_SHIRT, 2, 45.89, 45.89)
                .verifyHeaderCartCount(2));
    }

    @Test @DisplayName("PDP-06 - Continue shopping after adding a product")
    void continueShoppingAfterAdd() {
        runScenario(page -> {
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
        });
    }

    @Test @DisplayName("PDP-07 - Navigate to cart from confirmation dialog")
    void navigateToCartFromConfirmation() {
        runScenario(page -> new HomePage(page, "en")
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
                .verifyTotal(22.94));
    }
}
