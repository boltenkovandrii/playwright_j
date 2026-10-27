package com.prestashop.test.pages;

import com.microsoft.playwright.Page;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class AccountDashboardPage extends BaseStorefrontPage {

    public AccountDashboardPage(Page page, String locale) {
        super(page, locale);
    }

    @Override
    public AccountDashboardPage verifyLoaded() {
        super.verifyLoaded();
        assertThat(page.getByRole(com.microsoft.playwright.options.AriaRole.HEADING,
                new Page.GetByRoleOptions().setName(Texts.get(locale, "account")))).isVisible();
        return this;
    }

    @Override
    public AccountDashboardPage checkStructure() {
        super.checkStructure();
        assertThat(page.locator(".links .link-item")).hasCount(7);
        return this;
    }

}
