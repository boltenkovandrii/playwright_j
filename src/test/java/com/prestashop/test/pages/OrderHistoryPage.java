package com.prestashop.test.pages;

import com.prestashop.test.base.AllureArtifacts;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static com.microsoft.playwright.options.AriaRole.HEADING;

public class OrderHistoryPage extends BaseStorefrontPage {

    private final Locator heading;

    public OrderHistoryPage(Page page, String locale) {
        super(page, locale);
        heading = page.getByRole(HEADING, new Page.GetByRoleOptions().setName("Order history"));
    }

    @Override
    public OrderHistoryPage verifyLoaded() {
        AllureArtifacts.screenshot(page, "Order history page");
        super.verifyLoaded();
        assertThat(heading).isVisible();
        return this;
    }

    public OrderHistoryPage verifyOrderReferencePresent(String reference) {
        AllureArtifacts.screenshot(page, "Verifying order in history");
        assertThat(page.getByText(reference).and(page.locator(":visible")).first()).isVisible();
        return this;
    }
}
