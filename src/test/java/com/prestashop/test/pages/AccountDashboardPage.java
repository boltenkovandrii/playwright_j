package com.prestashop.test.pages;

import com.prestashop.test.base.AllureArtifacts;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static com.microsoft.playwright.options.AriaRole.HEADING;

public class AccountDashboardPage extends BaseStorefrontPage {

    private final Locator heading;
    private final Locator accountLinks;

    public AccountDashboardPage(Page page, String locale) {
        super(page, locale);
        heading = page.getByRole(HEADING, new Page.GetByRoleOptions().setName(Texts.get(locale, "account")));
        accountLinks = page.locator(".links .link-item");
    }

    public AccountDashboardPage open(String path) {
        page.navigate(localizedUrl(path));
        return verifyLoaded();
    }

    @Override
    public AccountDashboardPage verifyLoaded() {
        AllureArtifacts.screenshot(page, "Account dashboard page");
        super.verifyLoaded();
        assertThat(heading).isVisible();
        return this;
    }

    @Override
    @SuppressWarnings("checkstyle:MagicNumber")
    public AccountDashboardPage checkStructure() {
        AllureArtifacts.screenshot(page, "Checking account dashboard structure");
        super.checkStructure();
        assertThat(heading).isVisible();
        assertThat(accountLinks).hasCount(7);
        return this;
    }

    @Override
    public AccountDashboardPage verifyCurrentLanguage(String expectedLocale) {
        super.verifyCurrentLanguage(expectedLocale);
        assertThat(heading).isVisible();
        return this;
    }
}
