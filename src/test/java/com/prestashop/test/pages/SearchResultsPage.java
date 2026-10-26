package com.prestashop.test.pages;

import com.prestashop.test.components.ProductGrid;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class SearchResultsPage extends BaseStorefrontPage {

    private final ProductGrid products;
    private final Locator pageList;

    public SearchResultsPage(Page page, String locale) {
        super(page, locale);
        products = new ProductGrid(page.getByTestId("js-product-list"));
        pageList = page.locator("nav.pagination ul.page-list");
    }

    @Override
    public SearchResultsPage verifyLoaded() {
        super.verifyLoaded();
        assertThat(page.getByTestId("js-product-list-header")).isVisible();
        return this;
    }

    @Override
    public SearchResultsPage checkStructure() {
        super.checkStructure();
        if (!page.getByTestId("product-search-no-matches").isVisible()) {
            products.checkStructure();
        }
        return this;
    }

    public SearchResultsPage checkResultCount(int count) {
        assertThat(page.getByTestId("js-product-list").locator(".product-miniature")).hasCount(count);
        return this;
    }

    public SearchResultsPage verifyProductsMatch(String term) {
        for (Locator card : page.getByTestId("js-product-list").locator(".product-miniature").all()) {
            assertThat(card.locator(".product-title a")).containsText(term);
        }
        return this;
    }

    public SearchResultsPage checkNoMatches() {
        Locator message = page.getByTestId("product-search-no-matches");
        assertThat(message).isVisible();
        assertThat(message).containsText(Texts.get(locale, "noMatches"));
        return this;
    }

    public SearchResultsPage checkPaginationVisible(boolean visible) {
        if (visible) {
            assertThat(pageList).isVisible();
        } else {
            assertThat(pageList).isHidden();
        }
        return this;
    }

    public SearchResultsPage goToPage(int pageNumber) {
        pageList.getByRole(com.microsoft.playwright.options.AriaRole.LINK,
                new Locator.GetByRoleOptions().setName(String.valueOf(pageNumber)).setExact(true)).click();
        assertThat(page.locator("nav.pagination ul.page-list li.current a")).hasText(String.valueOf(pageNumber));
        return new SearchResultsPage(page, locale).verifyLoaded();
    }
}
