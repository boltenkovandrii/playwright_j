package com.prestashop.test.pages;

import com.microsoft.playwright.assertions.LocatorAssertions;
import com.prestashop.test.base.AllureArtifacts;
import com.microsoft.playwright.options.BoundingBox;
import com.prestashop.test.components.ProductGrid;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Mouse;
import com.microsoft.playwright.Page;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static com.microsoft.playwright.options.AriaRole.LINK;

public class CatalogPage extends BaseStorefrontPage {

    private static final int SLIDER_STEPS = 12;
    private static final int SLIDER_MAX_CORRECTIONS = 10;

    private final ProductGrid productGrid;
    private final Locator heading;
    private final Locator activeFilters;
    private final Locator pageList;
    private final Locator brandsHeader;

    public CatalogPage(Page page, String locale) {
        super(page, locale);
        productGrid = new ProductGrid(page.getByTestId("js-product-list"));
        heading = page.getByTestId("js-product-list-header");
        activeFilters = page.getByTestId("js-active-search-filters");
        pageList = page.locator("nav.pagination ul.page-list");
        brandsHeader = page.getByRole(LINK,
                new Page.GetByRoleOptions().setName(Texts.get(locale, "brands")));
    }

    @Override
    public CatalogPage verifyLoaded() {
        AllureArtifacts.screenshot(page, "Catalog page");
        super.verifyLoaded();
        productGrid.verifyLoaded();
        return this;
    }

    @Override
    public CatalogPage checkStructure() {
        AllureArtifacts.screenshot(page, "Checking catalog page structure");
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
        AllureArtifacts.screenshot(page, "Opening product by name: " + name);
        productGrid.openProductByName(name);
        return new ProductPage(page, locale).verifyLoaded();
    }

    public ProductPage openProduct(int index) {
        AllureArtifacts.screenshot(page, "Opening product");
        productGrid.productAt(index).openProduct();
        return new ProductPage(page, locale).verifyLoaded();
    }

    public CatalogPage sortBy(String criteria) {
        AllureArtifacts.screenshot(page, "Sorting by " + criteria);
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
        AllureArtifacts.screenshot(page, "Setting results per page: " + resultsPerPage);
        // Get the current URL and add resultsPerPage parameter.
        page.navigate(page.url() + (page.url().contains("?") ? "&" : "?") + "resultsPerPage=" + resultsPerPage);
        return new CatalogPage(page, locale).verifyLoaded();
    }

    public CatalogPage checkPaginationVisible(boolean visible) {
        AllureArtifacts.screenshot(page, "Checking pagination visibility");
        if (visible) {
            assertThat(pageList).isVisible();
        } else {
            assertThat(pageList).isHidden();
        }
        return this;
    }

    public CatalogPage goToPage(int pageNumber) {
        AllureArtifacts.screenshot(page, "Going to page number: " + pageNumber);
        pageList.getByRole(LINK,
                new Locator.GetByRoleOptions().setName(String.valueOf(pageNumber)).setExact(true)).click();
        Locator currentPage = page.locator("nav.pagination ul.page-list li.current a");
        // PrestaShop's pagination uses a resultsPerPage URL workaround. In headed mode some viewports
        // occasionally need a second click before navigation takes effect, so retry only if needed.
        try {
            assertThat(currentPage).hasText(String.valueOf(pageNumber), new LocatorAssertions.HasTextOptions().setTimeout(TIMEOUT));
        } catch (AssertionError error) {
            AllureArtifacts.screenshot(page, "Retrying click for page number: " + pageNumber);
            pageList.getByRole(LINK,
                    new Locator.GetByRoleOptions().setName(String.valueOf(pageNumber)).setExact(true)).click();
        }
        return new CatalogPage(page, locale).verifyLoaded();
    }

    public CatalogPage applyManufacturerFilter(String name) {
        AllureArtifacts.screenshot(page, "Applying manufacturer filter: " + name);
        openMobileFiltersIfNeeded("Brand");
        page.locator(".facet[data-name='Brand'] a").filter(new Locator.FilterOptions().setHasText(name)).click();
        closeMobileFiltersIfNeeded();
        return new CatalogPage(page, locale).verifyLoaded();
    }

    public CatalogPage applyPriceFilter(int minimum, int maximum) {
        AllureArtifacts.screenshot(page, "Applying price filter");
        openMobileFiltersIfNeeded("Price");
        dragPriceHandleIterative(0, minimum);
        dragPriceHandleIterative(1, maximum);
        closeMobileFiltersIfNeeded();
        return new CatalogPage(page, locale).verifyLoaded();
    }

    public CatalogPage verifyActiveFilter(String text) {
        if (viewportWidth() >= BOOTSTRAP_MD) {
            assertThat(activeFilters).isVisible();
            assertThat(activeFilters).containsText(text);
        }
        return this;
    }

    private void dragPriceHandle(int index, double target) {
        Locator facet = page.locator(".faceted-slider[data-slider-label='Price']");
        double minimum = Double.parseDouble(facet.getAttribute("data-slider-min"));
        double maximum = Double.parseDouble(facet.getAttribute("data-slider-max"));
        if (maximum == minimum) {
            throw new AssertionError("Price slider bounds are not distinct");
        }
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
        page.mouse().move(x, handleBox.y + handleBox.height / 2, new Mouse.MoveOptions().setSteps(SLIDER_STEPS));
        page.mouse().up();
    }


    private void dragPriceHandleIterative(int index, int target) {
        // The drag operation triggers an AJAX update/re-render, so we wait for the response before proceeding.
        page.waitForResponse(response -> response.url().contains("Price-"),
                () -> dragPriceHandle(index, target)
        );

        int currentValue = getPriceSliderValue(index);
        int counter = 0;
        // Viewport-dependent drag precision can require one-pixel corrections.
        while (getPriceSliderValue(index) != target && counter < SLIDER_MAX_CORRECTIONS) {
            AllureArtifacts.screenshot(page, "Applying price filter iteration " + counter + " for index " + index + " with target " + target);
            counter++;
            boolean increase = currentValue < target;
            movePriceHandlerOneStep(index, increase);
            currentValue = getPriceSliderValue(index);
        }
    }

    private void movePriceHandlerOneStep(int index, boolean increase) {
        Locator facet = page.locator(".faceted-slider[data-slider-label='Price']");
        double minimum = Double.parseDouble(facet.getAttribute("data-slider-min"));
        double maximum = Double.parseDouble(facet.getAttribute("data-slider-max"));

        Locator track = facet.locator(".ui-slider");
        Locator handle = facet.locator(".ui-slider-handle").nth(index);
        BoundingBox trackBox = track.boundingBox();
        BoundingBox handleBox = handle.boundingBox();

        double step = trackBox.width / (maximum - minimum);
        double x = handleBox.x + handleBox.width / 2 + (increase ? step : -step);

        // The drag operation triggers an AJAX update/re-render, so we wait for the response before proceeding.
        page.waitForResponse(response -> response.url().contains("Price-"),
                () -> {
                    handle.hover();
                    page.mouse().down();
                    page.mouse().move(x, handleBox.y + handleBox.height / 2, new Mouse.MoveOptions().setSteps(1));
                    page.mouse().up();
                });
    }

    private int getPriceSliderValue(int index) {
        Locator facet = page.locator(".faceted-slider[data-slider-label='Price']");
        String value = facet.getAttribute("data-slider-values");
        if (value == null) {
            throw new AssertionError("Price slider values are not available");
        }
        try {
            String[] values = value.replaceAll("[\\[\\]\"]", "").split(",");
            return Integer.parseInt(values[index].trim());
        } catch (NumberFormatException e) {
            throw new AssertionError("Failed to parse price slider values: " + value, e);
        }
    }

    private void openMobileFiltersIfNeeded(String filterName) {
        if (viewportWidth() < BOOTSTRAP_MD) {
            AllureArtifacts.screenshot(page, "Opening filters for mobile");
            page.getByTestId("search_filter_toggler").click();
            page.locator(".facet[data-name='" + filterName + "']").click();
        }
    }

    private void closeMobileFiltersIfNeeded() {
        if (viewportWidth() < BOOTSTRAP_MD) {
            AllureArtifacts.screenshot(page, "Closing filters for mobile");
            page.getByTestId("search_filter_controls").locator("button").click();
        }
    }

    @Override
    public CatalogPage verifyCurrentLanguage(String expectedLocale) {
        super.verifyCurrentLanguage(expectedLocale);
        assertThat(brandsHeader).isVisible();
        return this;
    }
}
