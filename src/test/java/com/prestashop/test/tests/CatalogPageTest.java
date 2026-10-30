package com.prestashop.test.tests;

import com.prestashop.test.base.TestBase;
import com.prestashop.test.pages.CatalogPage;
import com.prestashop.test.pages.HomePage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

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
            CatalogPage catalog = new HomePage(page, "en").open().openCategory("Accessories").checkResultCount(11);
            catalog.verifyCategoryName("Accessories");
            catalog.products().productAt(0).checkStructure();
            catalog.products().productAt(1).checkStructure();
            assertThat(catalog.products().names()).hasSizeGreaterThanOrEqualTo(2).doesNotHaveDuplicates();
        });
    }

    @Test
    @DisplayName("CAT-03 - Open product details page")
    void openProductDetailsPage() {
        runScenario(page -> new HomePage(page, "en")
                .open()
                .openCategory("Clothes")
                .openProductByName("Hummingbird printed t-shirt")
                .verifyProductName("Hummingbird printed t-shirt")
                .checkStructure());
    }

    @Test
    @DisplayName("CAT-04 - Change product sorting")
    void changeProductSorting() {
        runScenario(page -> {
            CatalogPage catalog = new HomePage(page, "en").open().openCategory("Accessories");
            List<Double> initial = catalog.products().prices();
            List<Double> sorted = catalog.sortBy("Price, high to low").products().prices();
            assertThat(sorted).isNotEqualTo(initial).isSortedAccordingTo(java.util.Comparator.reverseOrder());
        });
    }

    @Test
    @DisplayName("CAT-05 - Navigate through catalog pagination")
    void catalogPagination() {
        // don't have enough products, so using workaround to check pagination
        runScenario(page -> new HomePage(page, "en")
                .open()
                .openCategory("Accessories")
                .checkPaginationVisible(false)
                .setResultsPerPage(8)
                .checkResultCount(8)
                .checkPaginationVisible(true)
                .goToPage(2)
                .checkResultCount(3)
                .checkPaginationVisible(true));
    }

    @Test
    @DisplayName("CAT-06 - Navigate using breadcrumbs")
    void navigateUsingBreadcrumbs() {
        runScenario(page -> new HomePage(page, "en")
                .open()
                .openCategory("Clothes")
                .openProductByName("Hummingbird printed sweater")
                .goToCategoryFromBreadcrumb("Clothes")
                .verifyCategoryName("Clothes"));
    }

    @Test
    @DisplayName("CAT-07 - Filter products by price")
    void filterProductsByPrice() {
        runScenario(page -> new HomePage(page, "en")
                .open()
                .openCategory("Accessories")
                .checkResultCount(11)
                .applyPriceFilter(22, 38)
                .checkResultCount(3)
                .verifyActiveFilter("Price"));
    }

    @Test
    @DisplayName("CAT-08 - Filter products by manufacturer")
    void filterProductsByManufacturer() {
        runScenario(page -> new HomePage(page, "en")
                .open()
                .openCategory("Accessories")
                .checkResultCount(11)
                .applyManufacturerFilter("Studio Design")
                .checkResultCount(7)
                .verifyActiveFilter("Studio Design"));
    }
}
