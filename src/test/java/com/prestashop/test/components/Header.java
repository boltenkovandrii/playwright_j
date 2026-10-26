package com.prestashop.test.components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.SelectOption;
import com.prestashop.test.pages.Texts;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class Header {

    private final Page page;
    private final Locator container;
    private final Locator desktopLanguageSelector;
    private final Locator mobileLanguageSelector;
    private final Locator mobileMenu;
    private final Locator searchInput;

    public Header(Page page) {
        this.page = page;
        container = page.getByTestId("header");
        desktopLanguageSelector = page.getByTestId("_desktop_language_selector").locator("button");
        mobileLanguageSelector = page.getByTestId("_mobile_language_selector").locator("select");
        mobileMenu = page.getByTestId("mobile_top_menu_wrapper");
        searchInput = container.getByTestId("search_widget").locator("input[type='text']");
    }

    public Header verifyLoaded() {
        assertThat(container).isVisible();
        return this;
    }

    public Header verifyCurrentLanguage(String locale) {
        if (viewportWidth() < 992) {
            openMobileMenu();
            assertThat(mobileLanguageSelector.locator("option:checked")).hasText(Texts.get(locale, "language"));
            closeMobileMenu();
        } else {
            assertThat(desktopLanguageSelector).containsText(Texts.get(locale, "language"));
        }
        return this;
    }

    public Header verifySearchPlaceholder(String locale) {
        assertThat(searchInput).hasAttribute("placeholder", Texts.get(locale, "search"));
        return this;
    }

    public Header checkStructure() {
        assertThat(container).isVisible();
        assertThat(searchInput).isVisible();
        if (viewportWidth() < 992) {
            assertThat(page.getByTestId("menu-icon")).isVisible();
            assertThat(page.getByTestId("_mobile_cart")).isVisible();
        } else {
            assertThat(page.getByTestId("_desktop_cart")).isVisible();
            assertThat(desktopLanguageSelector).isVisible();
        }
        return this;
    }

    public Header selectLanguage(String currentLocale, String targetLocale) {
        if (currentLocale.equals(targetLocale)) {
            return this;
        }
        String language = Texts.get(targetLocale, "language");
        if (viewportWidth() < 992) {
            openMobileMenu();
            mobileLanguageSelector.selectOption(new SelectOption().setLabel(language));
        } else {
            desktopLanguageSelector.click();
            page.getByRole(com.microsoft.playwright.options.AriaRole.LINK,
                    new Page.GetByRoleOptions().setName(language).setExact(true)).click();
        }
        return this;
    }

    public Header clickCategory(String name) {
        Locator categoryLinks;
        if (viewportWidth() < 992) {
            openMobileMenu();
            categoryLinks = mobileMenu.locator(".category > a");
        } else {
            categoryLinks = page.getByTestId("top-menu").locator(".category > a");
        }
        categoryLinks.filter(new Locator.FilterOptions().setHasText(name)).first().click();
        return this;
    }

    public Header verifyCartCount(int expectedCount) {
        Locator count = viewportWidth() < 992
                ? page.getByTestId("_mobile_cart").locator(".cart-products-count")
                : page.getByTestId("_desktop_cart").locator(".cart-products-count");
        assertThat(count).hasText("(" + expectedCount + ")");
        return this;
    }

    public Header search(String query) {
        searchInput.fill(query);
        searchInput.press("Enter");
        return this;
    }

    public Header openAccountPage() {
        activeUserInfo().locator(".account").click();
        return this;
    }

    public Header openLoginPage() {
        activeUserInfo().locator("a").click();
        return this;
    }

    public Header verifyLoggedIn() {
        assertThat(activeUserInfo().locator(".account")).isVisible();
        return this;
    }

    public Header verifyNotLoggedIn() {
        assertThat(activeUserInfo().locator(".account")).isHidden();
        return this;
    }

    public Header signOut() {
        activeUserInfo().locator(".logout").click();
        return this;
    }

    private Header openMobileMenu() {
        page.getByTestId("menu-icon").click();
        assertThat(mobileMenu).isVisible();
        return this;
    }

    private Header closeMobileMenu() {
        page.getByTestId("menu-icon").click();
        assertThat(mobileMenu).isHidden();
        return this;
    }

    private Locator activeUserInfo() {
        return viewportWidth() < 992
                ? page.getByTestId("_mobile_user_info")
                : page.getByTestId("_desktop_user_info");
    }

    private int viewportWidth() {
        return ((Number) page.evaluate("window.innerWidth")).intValue();
    }
}
