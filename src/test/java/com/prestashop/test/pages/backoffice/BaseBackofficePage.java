package com.prestashop.test.pages.backoffice;

import com.microsoft.playwright.Page;
import com.prestashop.test.base.PlaywrightSettings;
import com.prestashop.test.pages.BasePage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BaseBackofficePage extends BasePage<BaseBackofficePage> {

    public BaseBackofficePage(Page page) {
        super(page);
    }

    public BaseBackofficePage open(String path) {
        page.navigate(PlaywrightSettings.baseUrl() + "/admin-dev/" + path);
        return verifyLoaded();
    }

    @Override
    public BaseBackofficePage verifyLoaded() {
        assertThat(page.locator("body")).isVisible();
        return this;
    }
}
