package com.prestashop.test.pages;

import com.microsoft.playwright.options.BoundingBox;
import com.prestashop.test.components.ProductGrid;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Mouse;
import com.microsoft.playwright.Page;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static com.microsoft.playwright.options.AriaRole.LINK;

public class CatalogPage extends BaseStorefrontPage {

    private final ProductGrid productGrid;
    private final Locator heading;
    private final Locator pageList;

    public CatalogPage(Page page, String locale) {
        super(page, locale);
        productGrid = new ProductGrid(page.getByTestId("js-product-list"));
        heading = page.locator("#js-product-list-header h1");
        pageList = page.locator("nav.pagination ul.page-list");
    }

    @Override
    public CatalogPage verifyLoaded() {
        super.verifyLoaded();
        productGrid.verifyLoaded();
        return this;
    }

    @Override
    public CatalogPage checkStructure() {
        super.checkStructure();
        productGrid.checkStructure();
        return this;
    }

    public CatalogPage verifyCategoryName(String name) {
        assertThat(heading).containsText(name);
        return this;
    }

    public CatalogPage checkSubcategories(String... names) {
        Locator links = page.locator(".subcategory-name");
        assertThat(links).hasCount(names.length);
        for (String name : names) {
            assertThat(links.filter(new Locator.FilterOptions().setHasText(Pattern.compile(name)))).isVisible();
        }
        return this;
    }

    public CatalogPage checkResultCount(int expectedCount) {
        productGrid.checkCount(expectedCount);
        return this;
    }

    public ProductGrid products() {
        return productGrid;
    }

    public ProductPage openProductByName(String name) {
        productGrid.openProductByName(name);
        return new ProductPage(page, locale).verifyLoaded();
    }

    public ProductPage openProduct(int index) {
        productGrid.productAt(index).openProduct();
        return new ProductPage(page, locale).verifyLoaded();
    }

    public CatalogPage sortBy(String criteria) {
        Locator link = page.locator(".products-sort-order .dropdown-menu a")
                .filter(new Locator.FilterOptions().setHasText(criteria));
        String href = link.getAttribute("href");
        if (href == null) {
            throw new AssertionError("Sort link does not have an href: " + criteria);
        }
        page.navigate(href);
        return new CatalogPage(page, locale).verifyLoaded();
    }

    public CatalogPage setResultsPerPage(int resultsPerPage) {
        page.navigate(page.url() + (page.url().contains("?") ? "&" : "?") + "resultsPerPage=" + resultsPerPage);
        return new CatalogPage(page, locale).verifyLoaded();
    }

    public CatalogPage checkPaginationVisible(boolean visible) {
        if (visible) {
            assertThat(pageList).isVisible();
        } else {
            assertThat(pageList).isHidden();
        }
        return this;
    }

    public CatalogPage goToPage(int pageNumber) {
        pageList.getByRole(LINK,
                new Locator.GetByRoleOptions().setName(String.valueOf(pageNumber)).setExact(true)).click();
        assertThat(page.locator("nav.pagination ul.page-list li.current a")).hasText(String.valueOf(pageNumber));
        return new CatalogPage(page, locale).verifyLoaded();
    }

    public CatalogPage applyManufacturerFilter(String name) {
        openMobileFiltersIfNeeded("Brand");
        page.locator(".facet[data-name='Brand'] a").filter(new Locator.FilterOptions().setHasText(name)).click();
        closeMobileFiltersIfNeeded();
        return new CatalogPage(page, locale).verifyLoaded();
    }

    public CatalogPage applyPriceFilter(double minimum, double maximum) {
        openMobileFiltersIfNeeded("Price");
        dragPriceHandle(0, minimum);
        dragPriceHandle(1, maximum);
        closeMobileFiltersIfNeeded();
        return new CatalogPage(page, locale).verifyLoaded();
    }

    public CatalogPage verifyActiveFilter(String text) {
        if (viewportWidth() >= BOOTSTRAP_LG) {
            assertThat(page.getByTestId("js-active-search-filters")).containsText(text);
        }
        return this;
    }

    private void dragPriceHandle(int index, double target) {
        Locator facet = page.locator(".faceted-slider[data-slider-label='Price']");
        double minimum = Double.parseDouble(facet.getAttribute("data-slider-min"));
        double maximum = Double.parseDouble(facet.getAttribute("data-slider-max"));
        Locator track = facet.locator(".ui-slider");
        Locator handle = facet.locator(".ui-slider-handle").nth(index);
        BoundingBox trackBox = track.boundingBox();
        BoundingBox handleBox = handle.boundingBox();
        if (trackBox == null || handleBox == null) {
            throw new AssertionError("Price slider is not ready");
        }
        double bounded = Math.max(minimum, Math.min(maximum, target));
        double x = trackBox.x + ((bounded - minimum) / (maximum - minimum)) * trackBox.width;
        handle.hover();
        page.mouse().down();
        page.mouse().move(x, handleBox.y + handleBox.height / 2, new Mouse.MoveOptions().setSteps(12));
        page.mouse().up();
    }

    private void openMobileFiltersIfNeeded(String filterName) {
        if (viewportWidth() < BOOTSTRAP_LG) {
            page.getByTestId("search_filter_toggler").click();
            page.locator(".facet[data-name='" + filterName + "']").click();
        }
    }

    private void closeMobileFiltersIfNeeded() {
        if (viewportWidth() < BOOTSTRAP_LG) {
            page.getByTestId("search_filter_controls").locator("button").click();
        }
    }
}
