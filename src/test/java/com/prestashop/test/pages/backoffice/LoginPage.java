package com.prestashop.test.pages.backoffice;

import com.microsoft.playwright.Page;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class LoginPage extends BaseBackofficePage {

    public LoginPage(Page page) {
        super(page);
    }

    @Override
    public LoginPage verifyLoaded() {
        super.verifyLoaded();
        assertThat(page.locator("form")).isVisible();
        return this;
    }
}
