package com.prestashop.test.tests;

import com.prestashop.test.base.TestBase;
import com.prestashop.test.pages.AccountDashboardPage;
import com.prestashop.test.pages.CartPage;
import com.prestashop.test.pages.CatalogPage;
import com.prestashop.test.pages.CheckoutPage;
import com.prestashop.test.pages.HomePage;
import com.prestashop.test.pages.LoginPage;
import com.prestashop.test.pages.ProductPage;
import com.prestashop.test.pages.RegistrationPage;
import com.prestashop.test.pages.SearchResultsPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("regression")
@DisplayName("PrestaShop storefront - Localization")
class LocalizationTest extends TestBase {

    private static final String DEMO_EMAIL = "pub@prestashop.com";
    private static final String DEMO_PASSWORD = "123456789";
    private static final String PRODUCT_NAME = "Hummingbird printed t-shirt";


    @Test
    @DisplayName("LOCA-01 - Switch storefront language")
    void switchStorefrontLanguage() {
        runScenario(page -> {
            HomePage home = new HomePage(page, "en").open();
            home.verifyCurrentLanguage("en").verifySearchPlaceholder();

            HomePage dutchHome = home.switchLanguage("nl");
            dutchHome.verifyCurrentLanguage("nl").verifySearchPlaceholder();

            CatalogPage catalog = dutchHome.openCategory("Clothes"); // Should be localized?
            catalog.verifyCurrentLanguage("nl");
        });
    }

    @Test
    @DisplayName("LOCA-02 - Preserve storefront language across core pages")
    void preserveStorefrontLanguageAcrossCorePages() {
        runScenario(page -> {
            HomePage home = new HomePage(page, "en").open().switchLanguage("nl");
            home.verifyCurrentLanguage("nl");

            CatalogPage catalog = home.openCategory("Clothes"); // Should be localized?
            catalog.verifyCurrentLanguage("nl");

            ProductPage product = catalog.openProductByName(PRODUCT_NAME).checkStructure();
            product.verifyCurrentLanguage("nl");

            CartPage cart = product.addToCart().proceedToCart().checkStructure();
            cart.verifyCurrentLanguage("nl");

            CheckoutPage checkout = cart.proceedToCheckout().checkStructure();
            checkout.verifyCurrentLanguage("nl");
        });
    }

    @Test
    @DisplayName("LOCA-03 - Verify localization of core storefront pages")
    void verifyLocalizationOfCoreStorefrontPages() {
        runScenario(page -> {
            HomePage home = new HomePage(page, "en").open().switchLanguage("nl");
            home.verifyCurrentLanguage("nl").verifySearchPlaceholder();

            SearchResultsPage searchResults = home.search("zzzz-no-match-12345");
            searchResults.verifyCurrentLanguage("nl");
            searchResults.checkNoMatches();

            CatalogPage catalog = home.openCategory("Clothes"); // Should be localized?
            catalog.verifyCurrentLanguage("nl");

            ProductPage product = catalog.openProductByName(PRODUCT_NAME).checkStructure();
            product.verifyCurrentLanguage("nl");

            CartPage cart = product.addToCart().proceedToCart().checkStructure();
            cart.verifyCurrentLanguage("nl");

            CheckoutPage checkout = cart.proceedToCheckout().checkStructure();
            checkout.verifyCurrentLanguage("nl");

            LoginPage login = new HomePage(page, "en").open().switchLanguage("nl").openLoginPage().checkStructure();
            login.verifyCurrentLanguage("nl");

            RegistrationPage registration = login.openRegistrationPage().checkStructure();
            registration.verifyCurrentLanguage("nl");

            HomePage authenticatedHome = new HomePage(page, "en")
                    .open()
                    .switchLanguage("nl")
                    .openLoginPage()
                    .login(DEMO_EMAIL, DEMO_PASSWORD);
            authenticatedHome.verifyCurrentLanguage("nl");

            AccountDashboardPage account = authenticatedHome.openAccountPage().checkStructure();
            account.verifyCurrentLanguage("nl");
        });
    }
}
