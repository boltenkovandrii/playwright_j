package com.prestashop.test.tests;

import com.microsoft.playwright.Page;
import com.prestashop.test.base.BaseTest;
import com.prestashop.test.pages.HomePage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.prestashop.test.data.TestData.PRODUCT_T_SHIRT;

@Tag("regression")
@DisplayName("PrestaShop storefront - Homepage")
class HomePageTest extends BaseTest {

    @Test
    @DisplayName("HOME-01 - Check home page structure.")
    void homePageStructure(Page page) {
        new HomePage(page, "en")
                .open()
                .checkStructure();
    }

    @Test
    @DisplayName("HOME-02 - Check featured products block structure.")
    void featuredProductsDisplayed(Page page) {
        new HomePage(page, "en")
                .open()
                .getFeaturedProducts()
                .checkStructure();
    }

    @Test
    @DisplayName("HOME-03 - Navigate to category from main menu.")
    void navigateToCategoryFromMainMenu(Page page) {
        new HomePage(page, "en")
                .open()
                .openCategory("Clothes")
                .verifyCategoryName("Clothes")
                .checkSubcategories("Men", "Women")
                .checkResultCount(2)
                .checkStructure();
    }

    @Test
    @DisplayName("HOME-04 - Open featured product details page.")
    void navigateToFeaturedProduct(Page page) {
        new HomePage(page, "en")
                .open()
                .openFeaturedProductByName(PRODUCT_T_SHIRT)
                .verifyProductName(PRODUCT_T_SHIRT)
                .checkStructure();
    }
}
