package com.prestashop.test.tests;

import com.microsoft.playwright.Page;
import com.prestashop.test.runner.CustomRunnerTest;
import com.prestashop.test.pages.CatalogPage;
import com.prestashop.test.pages.HomePage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("regression")
@DisplayName("PrestaShop storefront - Navigation and catalog")
class CatalogPageTest {

    @CustomRunnerTest
    @DisplayName("CAT-01 - Check catalog page structure.")
    void catalogPageStructure(Page page) {
        new HomePage(page, "en").open().openCategory("Clothes").checkStructure();
    }

    @CustomRunnerTest
    @DisplayName("CAT-02 - Browse products in a category.")
    @SuppressWarnings("checkstyle:MagicNumber")
    void browseProductsInCategory(Page page) {
        CatalogPage catalog = new HomePage(page, "en").open().openCategory("Accessories").checkResultCount(11);
        catalog.verifyCategoryName("Accessories");
        catalog.products().productAt(0).checkStructure();
        catalog.products().productAt(1).checkStructure();
        assertThat(catalog.products().names()).hasSizeGreaterThanOrEqualTo(2).doesNotHaveDuplicates();
    }

    @CustomRunnerTest
    @DisplayName("CAT-03 - Open product details page.")
    void openProductDetailsPage(Page page) {
        new HomePage(page, "en")
                .open()
                .openCategory("Clothes")
                .openProductByName("Hummingbird printed t-shirt")
                .verifyProductName("Hummingbird printed t-shirt")
                .checkStructure();
    }

    @CustomRunnerTest
    @DisplayName("CAT-04 - Change product sorting.")
    void changeProductSorting(Page page) {
        CatalogPage catalog = new HomePage(page, "en").open().openCategory("Accessories");
        List<Double> initial = catalog.products().prices();
        List<Double> sorted = catalog.sortBy("Price, high to low").products().prices();
        assertThat(sorted).isNotEqualTo(initial).isSortedAccordingTo(java.util.Comparator.reverseOrder());
    }

    @CustomRunnerTest
    @DisplayName("CAT-05 - Navigate through catalog pagination.")
    @SuppressWarnings("checkstyle:MagicNumber")
    void catalogPagination(Page page) {
        // don't have enough products, so using workaround to check pagination
        new HomePage(page, "en")
                .open()
                .openCategory("Accessories")
                .checkPaginationVisible(false)
                .setResultsPerPage(8)
                .checkResultCount(8)
                .checkPaginationVisible(true)
                .goToPage(2)
                .checkResultCount(3)
                .checkPaginationVisible(true);
    }

    @CustomRunnerTest
    @DisplayName("CAT-06 - Navigate using breadcrumbs.")
    void navigateUsingBreadcrumbs(Page page) {
        new HomePage(page, "en")
                .open()
                .openCategory("Clothes")
                .openProductByName("Hummingbird printed sweater")
                .goToCategoryFromBreadcrumb("Clothes")
                .verifyCategoryName("Clothes");
    }

    @CustomRunnerTest
    @DisplayName("CAT-07 - Filter products by price.")
    @SuppressWarnings("checkstyle:MagicNumber")
    void filterProductsByPrice(Page page) {
        new HomePage(page, "en")
                .open()
                .openCategory("Accessories")
                .checkResultCount(11)
                .applyPriceFilter(22, 38)
                .checkResultCount(3)
                .verifyActiveFilter("Price");
    }

    @CustomRunnerTest
    @DisplayName("CAT-08 - Filter products by manufacturer.")
    @SuppressWarnings("checkstyle:MagicNumber")
    void filterProductsByManufacturer(Page page) {
        new HomePage(page, "en")
                .open()
                .openCategory("Accessories")
                .checkResultCount(11)
                .applyManufacturerFilter("Studio Design")
                .checkResultCount(7)
                .verifyActiveFilter("Studio Design");
    }
}
