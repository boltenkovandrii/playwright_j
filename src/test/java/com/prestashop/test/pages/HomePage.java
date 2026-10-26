package com.prestashop.test.pages;

import com.prestashop.test.base.AllureArtifacts;
import com.prestashop.test.components.ProductGrid;
import com.microsoft.playwright.Page;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class HomePage extends BaseStorefrontPage {

    private final ProductGrid featuredProducts;

    public HomePage(Page page, String locale) {
        super(page, locale);
        featuredProducts = new ProductGrid(page.locator(".featured-products"));
    }

    @Override
    public HomePage open() {
        super.open();
        return this;
    }

    @Override
    public HomePage verifyLoaded() {
        super.verifyLoaded();
        assertThat(page.getByTestId("carousel")).isVisible();
        featuredProducts.verifyLoaded();
        return this;
    }

    @Override
    public HomePage checkStructure() {
        AllureArtifacts.screenshot(page, "Checking home page structure");
        super.checkStructure();
        assertThat(page.getByTestId("carousel")).isVisible();
        featuredProducts.checkStructure();
        return this;
    }

    public ProductGrid featuredProducts() {
        return featuredProducts;
    }

    public CatalogPage openCategory(String name) {
        header.clickCategory(name);
        return new CatalogPage(page, locale).verifyLoaded();
    }

    public ProductPage openFeaturedProductByName(String name) {
        featuredProducts.openProductByName(name);
        return new ProductPage(page, locale).verifyLoaded();
    }

    public SearchResultsPage openSearchResults(String query, Integer resultsPerPage, Integer pageNumber) {
        String path = "search?search_query=" + URLEncoder.encode(query, StandardCharsets.UTF_8);
        if (resultsPerPage != null) {
            path += "&resultsPerPage=" + resultsPerPage;
        }
        if (pageNumber != null) {
            path += "&page=" + pageNumber;
        }
        page.navigate(localizedUrl(path));
        return new SearchResultsPage(page, locale).verifyLoaded();
    }
}
