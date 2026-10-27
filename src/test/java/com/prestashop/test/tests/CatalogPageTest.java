package com.prestashop.test.tests;

import com.prestashop.test.base.TestBase;
import com.prestashop.test.pages.HomePage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("regression")
@DisplayName("PrestaShop storefront - Navigation and catalog")
class CatalogPageTest extends TestBase {

    @Test
    @DisplayName("CAT-01 - Check catalog page structure")
    void catalogPageStructure() {
        runScenario(page -> new HomePage(page, "en").open().openCategory("Clothes").checkStructure());
    }

    @Test
    @DisplayName("CAT-02 - Browse products in a category")
    void browseProductsInCategory() {
        runScenario(page -> {
            var catalog = new HomePage(page, "en").open().openCategory("Accessories").checkResultCount(11);
            catalog.products().productAt(0).checkStructure();
            catalog.products().productAt(1).checkStructure();
            assertThat(catalog.products().names()).hasSizeGreaterThanOrEqualTo(2).doesNotHaveDuplicates();
        });
    }

    @Test
    @DisplayName("CAT-04 - Change product sorting")
    void changeProductSorting() {
        runScenario(page -> {
            var catalog = new HomePage(page, "en").open().openCategory("Accessories");
            var initial = catalog.products().prices();
            var sorted = catalog.sortBy("Price, high to low").products().prices();
            assertThat(sorted).isNotEqualTo(initial).isSortedAccordingTo(java.util.Comparator.reverseOrder());
        });
    }

    @Test
    @DisplayName("CAT-05 - Navigate through catalog pagination")
    void catalogPagination() {
        runScenario(page -> new HomePage(page, "en").open().openCategory("Accessories")
                .checkPaginationVisible(false).setResultsPerPage(8).checkResultCount(8)
                .checkPaginationVisible(true).goToPage(2).checkResultCount(3));
    }

    @Test
    @DisplayName("CAT-07 - Filter products by price")
    void filterProductsByPrice() {
        runScenario(page -> new HomePage(page, "en").open().openCategory("Accessories")
                .checkResultCount(11)
                .applyPriceFilter(22, 38).checkResultCount(3).verifyActiveFilter("Price"));
    }

    @Test
    @DisplayName("CAT-08 - Filter products by manufacturer")
    void filterProductsByManufacturer() {
        runScenario(page -> new HomePage(page, "en").open().openCategory("Accessories")
                .checkResultCount(11).applyManufacturerFilter("Studio Design").checkResultCount(7)
                .verifyActiveFilter("Studio Design"));
    }
}
