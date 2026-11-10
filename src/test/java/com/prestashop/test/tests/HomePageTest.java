package com.prestashop.test.tests;

import com.microsoft.playwright.Page;
import com.prestashop.test.runner.CustomRunnerTest;
import com.prestashop.test.pages.HomePage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;

@Tag("regression")
@DisplayName("PrestaShop storefront - Homepage")
class HomePageTest {

    @CustomRunnerTest
    @DisplayName("HOME-01 - Check home page structure.")
    void homePageStructure(Page page) {
        new HomePage(page, "en")
                .open()
                .checkStructure();
    }

    @CustomRunnerTest
    @DisplayName("HOME-02 - Check featured products block structure.")
    void featuredProductsDisplayed(Page page) {
        new HomePage(page, "en")
                .open()
                .getFeaturedProducts()
                .checkStructure();
    }

    @CustomRunnerTest
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

    @CustomRunnerTest
    @DisplayName("HOME-04 - Open featured product details page.")
    void navigateToFeaturedProduct(Page page) {
        new HomePage(page, "en")
                .open()
                .openFeaturedProductByName("Hummingbird printed t-shirt")
                .verifyProductName("Hummingbird printed t-shirt")
                .checkStructure();
    }
}
