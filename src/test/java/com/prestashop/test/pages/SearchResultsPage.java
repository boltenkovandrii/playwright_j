package com.prestashop.test.pages;

import com.microsoft.playwright.assertions.LocatorAssertions;
import com.prestashop.test.base.AllureArtifacts;
import com.prestashop.test.components.ProductGrid;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static com.microsoft.playwright.options.AriaRole.LINK;

public class SearchResultsPage extends BaseStorefrontPage {

    private final ProductGrid products;
    private final Locator heading;
    private final Locator pageList;
    private final Locator noMatchesMessage;

    public SearchResultsPage(Page page, String locale) {
        super(page, locale);
        products = new ProductGrid(page.getByTestId("js-product-list"));
        heading = page.getByTestId("js-product-list-header");
        pageList = page.locator("nav.pagination ul.page-list");
        noMatchesMessage = page.getByTestId("product-search-no-matches");
    }

    @Override
    public SearchResultsPage verifyLoaded() {
        AllureArtifacts.screenshot(page, "Search results page");
        super.verifyLoaded();
        assertThat(heading).isVisible();
        return this;
    }

    @Override
    public SearchResultsPage checkStructure() {
        AllureArtifacts.screenshot(page, "Checking search results page structure");
        super.checkStructure();
        assertThat(heading).isVisible();
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
        AllureArtifacts.screenshot(page, "Verifying products match: " + term);
        products.verifyNamesContain(term);
        return this;
    }

    public SearchResultsPage checkNoMatches() {
        AllureArtifacts.screenshot(page, "Checking no matches message");
        assertThat(noMatchesMessage).isVisible();
        assertThat(noMatchesMessage).containsText(Texts.get(locale, "noMatches"));
        return this;
    }

    public SearchResultsPage checkPaginationVisible(boolean visible) {
        AllureArtifacts.screenshot(page, "Checking pagination visibility");
        if (visible) {
            assertThat(pageList).isVisible();
        } else {
            assertThat(pageList).isHidden();
        }
        return this;
    }

    public SearchResultsPage goToPage(int pageNumber) {
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
        return new SearchResultsPage(page, locale).verifyLoaded();
    }

    @Override
    public SearchResultsPage verifyCurrentLanguage(String expectedLocale) {
        super.verifyCurrentLanguage(expectedLocale);
        assertThat(heading).containsText(Texts.get(locale, "searchResults"));
        return this;
    }
}
