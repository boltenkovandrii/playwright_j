package com.prestashop.test.pages;

import com.prestashop.test.base.AllureArtifacts;
import com.prestashop.test.base.PlaywrightSettings;
import com.prestashop.test.components.Footer;
import com.prestashop.test.components.Header;
import com.microsoft.playwright.Page;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BaseStorefrontPage extends BasePage<BaseStorefrontPage> {

    protected final String locale;
    protected final Header header;
    protected final Footer footer;

    protected BaseStorefrontPage(Page page, String locale) {
        super(page);
        this.locale = locale;
        this.header = new Header(page);
        this.footer = new Footer(page, locale);
    }

    public BaseStorefrontPage open() {
        page.navigate(localizedUrl(""));
        return verifyLoaded();
    }

    @Override
    public BaseStorefrontPage verifyLoaded() {
        assertThat(page.getByTestId("main")).isVisible();
        header.verifyLoaded();
        header.verifyCurrentLanguage(locale);
        return this;
    }

    public BaseStorefrontPage verifyCurrentLanguage(String expectedLocale) {
        header.verifyCurrentLanguage(expectedLocale);
        return this;
    }

    public BaseStorefrontPage verifySearchPlaceholder() {
        header.verifySearchPlaceholder(locale);
        return this;
    }

    public BaseStorefrontPage verifyHeaderCartCount(int count) {
        header.verifyCartCount(count);
        return this;
    }

    public BaseStorefrontPage checkStructure() {
        assertThat(page.getByTestId("main")).isVisible();
        header.checkStructure(locale);
        header.verifyCurrentLanguage(locale);
        footer.checkStructure();
        return this;
    }

    public HomePage switchLanguage(String targetLocale) {
        AllureArtifacts.screenshot(page, "Switching storefront language");
        header.selectLanguage(locale, targetLocale);
        return new HomePage(page, targetLocale).verifyLoaded();
    }

    public LoginPage openLoginPage() {
        header.openLoginPage();
        return new LoginPage(page, locale).verifyLoaded();
    }

    public AccountDashboardPage openAccountPage() {
        header.openAccountPage();
        return new AccountDashboardPage(page, locale).verifyLoaded();
    }

    public BaseStorefrontPage signOut() {
        if (viewportWidth() < BOOTSTRAP_MD) {
            footer.signOut();
        } else {
            header.signOut();
        }
        return new BaseStorefrontPage(page, locale).verifyLoaded();
    }

    public BaseStorefrontPage navigateToAccountDashboardDirectly() {
        page.navigate(localizedUrl("my-account"));
        return new BaseStorefrontPage(page, locale).verifyLoaded();
    }

    public SearchResultsPage search(String query) {
        header.search(query);
        return new SearchResultsPage(page, locale).verifyLoaded();
    }

    public LoginPage asLoginPage() {
        return new LoginPage(page, locale).verifyLoaded();
    }

    public RegistrationPage asRegistrationPage() {
        return new RegistrationPage(page, locale).verifyLoaded();
    }

    public HomePage asHomePage() {
        return new HomePage(page, locale).verifyLoaded();
    }

    protected String localizedUrl(String path) {
        String normalized = path.replaceFirst("^/", "");
        return PlaywrightSettings.baseUrl() + "/" + locale + "/" + normalized;
    }

    protected int viewportWidth() {
        return ((Number) page.evaluate("window.innerWidth")).intValue();
    }

    public BaseStorefrontPage verifyLoggedIn() {
        header.verifyLoggedIn();
        return this;
    }

    public BaseStorefrontPage verifyNotLoggedIn() {
        header.verifyNotLoggedIn();
        return this;
    }

}
