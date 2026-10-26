package com.prestashop.test.pages.backoffice;

import com.microsoft.playwright.Page;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class DashboardPage extends BaseBackofficePage {

    public DashboardPage(Page page) {
        super(page);
    }

    @Override
    public DashboardPage verifyLoaded() {
        super.verifyLoaded();
        assertThat(page.getByTestId("content")).isVisible();
        return this;
    }
}
