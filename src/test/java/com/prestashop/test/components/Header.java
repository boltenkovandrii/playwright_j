package com.prestashop.test.components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.SelectOption;
import com.prestashop.test.base.AllureArtifacts;
import com.prestashop.test.base.AriaSnapshots;
import com.prestashop.test.pages.Texts;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static com.microsoft.playwright.options.AriaRole.LINK;
import static com.prestashop.test.pages.BasePage.BOOTSTRAP_MD;

public class Header {

    private final Page page;
    private final Locator container;
    private final Locator desktopLanguageSelector;
    private final Locator mobileLanguageSelector;
    private final Locator menuButton;
    private final Locator mobileMenu;
    private final Locator searchInput;
    private final Locator desktopCart;
    private final Locator mobileCart;
    private final Locator desktopCartProductsCount;
    private final Locator mobileCartProductsCount;
    private final Locator desktopUserInfo;
    private final Locator mobileUserInfo;

    public Header(Page page) {
        this.page = page;
        container = page.getByTestId("header");
        desktopLanguageSelector = page.getByTestId("_desktop_language_selector").locator("button");
        mobileLanguageSelector = page.getByTestId("_mobile_language_selector").locator("select");
        menuButton = page.getByTestId("menu-icon");
        mobileMenu = page.getByTestId("mobile_top_menu_wrapper");
        searchInput = container.getByTestId("search_widget").locator("input[type='text']");
        desktopCart = page.getByTestId("_desktop_cart");
        mobileCart = page.getByTestId("_mobile_cart");
        desktopCartProductsCount = desktopCart.locator(".cart-products-count");
        mobileCartProductsCount = mobileCart.locator(".cart-products-count");
        desktopUserInfo = page.getByTestId("_desktop_user_info");
        mobileUserInfo = page.getByTestId("_mobile_user_info");
    }

    public Header verifyLoaded() {
        assertThat(container).isVisible();
        return this;
    }

    public Header verifyCurrentLanguage(String locale) {
        if (viewportWidth() < BOOTSTRAP_MD) {
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

    public Header checkStructure(String locale) {
        AllureArtifacts.screenshot(container, "Checking header structure");
        assertThat(container).isVisible();
        assertThat(searchInput).isVisible();
        if (viewportWidth() < BOOTSTRAP_MD) {
            assertThat(menuButton).isVisible();
            assertThat(mobileCart).isVisible();
            assertThat(desktopCart).isHidden();
            assertThat(desktopLanguageSelector).isHidden();
            assertThat(container).matchesAriaSnapshot(AriaSnapshots.load("header_compact", locale));
        } else {
            assertThat(menuButton).isHidden();
            assertThat(mobileCart).isHidden();
            assertThat(desktopCart).isVisible();
            assertThat(desktopLanguageSelector).isVisible();
            assertThat(container).matchesAriaSnapshot(AriaSnapshots.load("header", locale));
        }
        return this;
    }

    public Header selectLanguage(String currentLocale, String targetLocale) {
        if (currentLocale.equals(targetLocale)) {
            return this;
        }
        String language = Texts.get(targetLocale, "language");
        if (viewportWidth() < BOOTSTRAP_MD) {
            openMobileMenu();
            mobileLanguageSelector.selectOption(new SelectOption().setLabel(language));
        } else {
            desktopLanguageSelector.click();
            page.getByRole(LINK,
                    new Page.GetByRoleOptions().setName(language).setExact(true)).click();
        }
        return this;
    }

    public Header clickCategory(String name) {
        Locator categoryLinks;
        if (viewportWidth() < BOOTSTRAP_MD) {
            openMobileMenu();
            categoryLinks = mobileMenu.locator(".category > a");
        } else {
            categoryLinks = page.getByTestId("top-menu").locator(".category > a");
        }
        categoryLinks.filter(new Locator.FilterOptions().setHasText(name)).first().click();
        return this;
    }

    public Header verifyCartCount(int expectedCount) {
        Locator count = viewportWidth() < BOOTSTRAP_MD ? mobileCartProductsCount : desktopCartProductsCount;
        assertThat(count).isVisible();
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

    public String accountName() {
        String accountName = activeUserInfo().locator(".account span").textContent();
        if (accountName == null) {
            throw new AssertionError("Account name text is not available");
        }
        return accountName.strip();
    }

    public Header signOut() {
        activeUserInfo().locator(".logout").click();
        return this;
    }

    private Header openMobileMenu() {
        menuButton.click();
        assertThat(mobileMenu).isVisible();
        return this;
    }

    private Header closeMobileMenu() {
        menuButton.click();
        assertThat(mobileMenu).isHidden();
        return this;
    }

    private Locator activeUserInfo() {
        return viewportWidth() < BOOTSTRAP_MD
                ? mobileUserInfo
                : desktopUserInfo;
    }

    private int viewportWidth() {
        return ((Number) page.evaluate("window.innerWidth")).intValue();
    }
}
