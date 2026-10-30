package com.prestashop.test.pages.backoffice;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class DashboardPage extends BaseBackofficePage {

    private final Locator dashboard;

    public DashboardPage(Page page) {
        super(page);
        dashboard = page.getByTestId("content");
    }

    @Override
    public DashboardPage open(String path) {
        super.open(path);
        return this;
    }

    @Override
    public DashboardPage verifyLoaded() {
        super.verifyLoaded();
        assertThat(dashboard).isVisible();
        return this;
    }
}
