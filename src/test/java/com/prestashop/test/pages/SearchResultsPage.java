package com.prestashop.test.pages;

import com.prestashop.test.components.ProductGrid;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static com.microsoft.playwright.options.AriaRole.LINK;

public class SearchResultsPage extends BaseStorefrontPage {

    private final ProductGrid products;
    private final Locator pageList;
    private final Locator noMatchesMessage;

    public SearchResultsPage(Page page, String locale) {
        super(page, locale);
        products = new ProductGrid(page.getByTestId("js-product-list"));
        pageList = page.locator("nav.pagination ul.page-list");
        noMatchesMessage = page.getByTestId("product-search-no-matches");
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
        if (!noMatchesMessage.isVisible()) {
            products.checkStructure();
        }
        return this;
    }

    public SearchResultsPage checkResultCount(int count) {
        products.checkCount(count);
        return this;
    }

    public SearchResultsPage verifyProductsMatch(String term) {
        products.verifyNamesContain(term);
        return this;
    }

    public SearchResultsPage checkNoMatches() {
        assertThat(noMatchesMessage).isVisible();
        assertThat(noMatchesMessage).containsText(Texts.get(locale, "noMatches"));
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
        pageList.getByRole(LINK,
                new Locator.GetByRoleOptions().setName(String.valueOf(pageNumber)).setExact(true)).click();
        assertThat(page.locator("nav.pagination ul.page-list li.current a")).hasText(String.valueOf(pageNumber));
        return new SearchResultsPage(page, locale).verifyLoaded();
    }
}
