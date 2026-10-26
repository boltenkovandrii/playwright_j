package com.prestashop.test.components;

import com.microsoft.playwright.Page;
import com.prestashop.test.pages.Texts;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class Footer {

    private final Page page;
    private final String locale;

    public Footer(Page page, String locale) {
        this.page = page;
        this.locale = locale;
    }

    public Footer checkStructure() {
        assertThat(page.getByTestId("footer")).isVisible();
        return this;
    }

    public Footer signOut() {
        if (viewportWidth() < 992) {
            page.getByTestId("block_myaccount_infos").click();
        }
        page.getByTestId("block_myaccount_infos")
                .getByRole(com.microsoft.playwright.options.AriaRole.LINK,
                        new com.microsoft.playwright.Locator.GetByRoleOptions().setName(Texts.get(locale, "signOut"))).click();
        return this;
    }

    private int viewportWidth() {
        return ((Number) page.evaluate("window.innerWidth")).intValue();
    }
}
