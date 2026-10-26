package com.prestashop.test.tests;

import com.prestashop.test.base.TestBase;
import com.prestashop.test.pages.HomePage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("regression")
@DisplayName("PrestaShop storefront - Homepage")
class HomePageTest extends TestBase {

    @Test
    @DisplayName("HOME-01 - Check home page structure")
    void homePageStructure() {
        runScenario(page -> new HomePage(page, "en").open().checkStructure());
    }

    @Test
    @DisplayName("HOME-02 - Check featured products block structure")
    void featuredProductsDisplayed() {
        runScenario(page -> new HomePage(page, "en").open().featuredProducts().checkStructure());
    }

    @Test
    @DisplayName("HOME-03 - Navigate to category from main menu")
    void navigateToCategoryFromMainMenu() {
        runScenario(page -> new HomePage(page, "en").open().openCategory("Clothes")
                .verifyCategoryName("Clothes").checkSubcategories("Men", "Women").checkResultCount(2).checkStructure());
    }

    @Test
    @DisplayName("HOME-04 - Open featured product details page")
    void navigateToFeaturedProduct() {
        runScenario(page -> new HomePage(page, "en").open().openFeaturedProductByName("Hummingbird printed t-shirt")
                .verifyProductName("Hummingbird printed t-shirt").checkStructure());
    }
}
