package com.prestashop.test.pages.backoffice;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class LoginPage extends BaseBackofficePage {

    private final Locator loginForm;

    public LoginPage(Page page) {
        super(page);
        loginForm = page.locator("form");
    }

    @Override
    public LoginPage open() {
        super.open();
        return this;
    }

    @Override
    public LoginPage open(String path) {
        super.open(path);
        return this;
    }

    @Override
    public LoginPage verifyLoaded() {
        super.verifyLoaded();
        assertThat(loginForm).isVisible();
        return this;
    }
}
