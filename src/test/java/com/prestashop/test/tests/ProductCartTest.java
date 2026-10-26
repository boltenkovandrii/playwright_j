package com.prestashop.test.tests;

import com.prestashop.test.base.TestBase;
import com.prestashop.test.pages.HomePage;
import com.prestashop.test.pages.ProductSpec;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("regression")
@DisplayName("PrestaShop storefront - Product details and cart")
class ProductCartTest extends TestBase {

    @Test
    @DisplayName("PDP-01/02 - Check product page and product information")
    void productDetails() {
        runScenario(page -> new HomePage(page, "en").open().openCategory("Clothes")
                .openProductByName("Hummingbird printed sweater")
                .checkStructure().verifyProductName("Hummingbird printed sweater")
                .verifyRegularPrice(43.08).verifyPrice(34.46)
                .verifyShortDescription("Regular fit, round neckline, long sleeves. 100% cotton")
                .verifyInStockCount(1200));
    }

    @Test
    @DisplayName("PDP-03/04 - Change quantity and product combination")
    void quantityAndCombination() {
        runScenario(page -> {
            var product = new HomePage(page, "en").open().openCategory("Clothes")
                    .openProductByName("Hummingbird printed t-shirt");
            product.verifyQuantity(1).increaseQuantity().verifyQuantity(2)
                    .decreaseQuantity().verifyQuantity(1).setQuantity(3).verifyQuantity(3);
            product.verifySizeOptions("S", "M", "L", "XL").verifyColorOptionCount(2)
                    .selectSize("M").verifySelectedSize("M");
            String original = product.imageSource();
            product.selectColor("Black").verifySelectedColor("Black");
            assertThat(product.imageSource()).isNotEqualTo(original);
        });
    }

    @Test
    @DisplayName("PDP-05/06/07 - Add product, continue shopping, and open cart")
    void addProductAndOpenCart() {
        runScenario(page -> {
            var product = new HomePage(page, "en").open().openCategory("Clothes")
                    .openProductByName("Hummingbird printed t-shirt");
            product.addToCart().verifyAddToCartConfirmation("Hummingbird printed t-shirt", 1, 22.94, 22.94)
                    .continueShopping().addToCart();
            product.proceedToCart().verifyProductCount(1)
                    .verifyProductQuantity(new ProductSpec("Hummingbird printed t-shirt"), 2)
                    .verifyProductsSubtotal(45.89).verifyTotal(45.89);
        });
    }

    @Test
    @DisplayName("CART-01 to CART-07 - Cart operations")
    void cartOperations() {
        runScenario(page -> {
            var cart = new HomePage(page, "en").open().openCategory("Clothes")
                    .openProductByName("Hummingbird printed t-shirt").addToCart().proceedToCart();
            ProductSpec product = new ProductSpec("Hummingbird printed t-shirt");
            cart.checkStructure().verifyProductCount(1).verifyProductQuantity(product, 1)
                    .setProductQuantity(product, 2).verifyProductQuantity(product, 2)
                    .verifyProductsSubtotal(45.89).verifyTotal(45.89)
                    .removeProduct(product).checkEmptyStructure();
        });
    }
}
