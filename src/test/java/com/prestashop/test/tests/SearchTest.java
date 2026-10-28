package com.prestashop.test.tests;

import com.prestashop.test.base.TestBase;
import com.prestashop.test.pages.HomePage;
import com.prestashop.test.pages.SearchResultsPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("regression")
@DisplayName("PrestaShop storefront - Search")
class SearchTest extends TestBase {

    private static final String PRODUCT_NAME = "Hummingbird printed t-shirt";

    @Test
    @DisplayName("SRCH-01 - Search by full product name")
    void searchByFullProductName() {
        runScenario(page -> {
            new HomePage(page, "en")
                    .open()
                    .search(PRODUCT_NAME)
                    .checkStructure()
                    .verifyProductsMatch(PRODUCT_NAME)
                    .checkResultCount(1);
        });
    }

    @Test
    @DisplayName("SRCH-02 - Search by partial product name")
    void searchByPartialProductName() {
        runScenario(page -> {
            new HomePage(page, "en")
                    .open()
                    .search("Hummingbird")
                    .checkStructure()
                    .verifyProductsMatch("Hummingbird")
                    .checkResultCount(5);
        });
    }

    @Test
    @DisplayName("SRCH-03 - Search with no matching results")
    void searchWithNoMatchingResults() {
        runScenario(page -> {
            new HomePage(page, "en")
                    .open()
                    .search("zzzz-no-match-12345")
                    .checkStructure()
                    .checkResultCount(0)
                    .checkNoMatches()
                    .search("Hummingbird")
                    .checkResultCount(5);
        });
    }

    @Test
    @DisplayName("SRCH-04 - Search from catalog page")
    void searchFromCatalogPage() {
        runScenario(page -> {
            new HomePage(page, "en")
                    .open()
                    .openCategory("Clothes")
                    .search("Hummingbird")
                    .checkStructure()
                    .verifyProductsMatch("Hummingbird")
                    .checkResultCount(5);
        });
    }

    @Test
    @DisplayName("SRCH-05 - Navigate through search pagination")
    void navigateSearchPagination() {
        runScenario(page -> {
            // don't have enough products, so using workaround to check pagination
            HomePage home = new HomePage(page, "en").open();
            SearchResultsPage searchResults = home.openSearchResults("home", null, null);
            searchResults.checkPaginationVisible(false);

            searchResults = home.openSearchResults("home", 8, null);
            searchResults.checkResultCount(8).checkPaginationVisible(true);

            searchResults.goToPage(2).checkResultCount(3).checkPaginationVisible(true);
        });
    }

}
