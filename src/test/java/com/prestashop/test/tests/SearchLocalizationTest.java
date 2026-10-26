package com.prestashop.test.tests;

import com.prestashop.test.base.TestBase;
import com.prestashop.test.pages.HomePage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("regression")
@DisplayName("PrestaShop storefront - Search and localization")
class SearchLocalizationTest extends TestBase {

    @Test
    @DisplayName("SRCH-01 to SRCH-05 - Search and paginate results")
    void searchAndPagination() {
        runScenario(page -> {
            var home = new HomePage(page, "en").open();
            home.search("Hummingbird printed t-shirt").checkStructure().verifyProductsMatch("Hummingbird printed t-shirt")
                    .checkResultCount(1);
            home.search("zzzz-no-match-12345").checkResultCount(0).checkNoMatches();
            home.openSearchResults("home", 8, null).checkResultCount(8).checkPaginationVisible(true)
                    .goToPage(2).checkResultCount(3);
        });
    }

    @Test
    @DisplayName("LOCA-01 to LOCA-03 - Switch and preserve storefront language")
    void localization() {
        runScenario(page -> {
            var home = new HomePage(page, "en").open();
            home.verifyCurrentLanguage("en").verifySearchPlaceholder();
            var dutchHome = home.switchLanguage("nl");
            dutchHome.verifyCurrentLanguage("nl").verifySearchPlaceholder();
            var catalog = dutchHome.openCategory("Clothes");
            catalog.verifyCurrentLanguage("nl");
            var product = catalog.openProductByName("Hummingbird printed t-shirt");
            product.verifyCurrentLanguage("nl").checkStructure();
            var cart = product.addToCart().proceedToCart();
            cart.verifyCurrentLanguage("nl");
            cart.proceedToCheckout().verifyLoaded().checkStructure();
        });
    }
}
