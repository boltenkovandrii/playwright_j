package com.prestashop.test.pages;

import com.prestashop.test.base.AllureArtifacts;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static com.microsoft.playwright.options.AriaRole.BUTTON;
import static com.microsoft.playwright.options.AriaRole.HEADING;
import static com.microsoft.playwright.options.AriaRole.LINK;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginPage extends BaseStorefrontPage {

    private final Locator heading;
    private final Locator emailInput;
    private final Locator passwordInput;
    private final Locator submitButton;
    private final Locator registerLink;
    private final Locator errorMessage;

    public LoginPage(Page page, String locale) {
        super(page, locale);
        heading = page.getByRole(HEADING, new Page.GetByRoleOptions().setName(Texts.get(locale, "login")));
        emailInput = page.getByTestId("field-email");
        passwordInput = page.getByTestId("field-password");
        submitButton = page.getByRole(BUTTON, new Page.GetByRoleOptions().setName(Texts.get(locale, "signIn")));
        registerLink = page.getByRole(LINK, new Page.GetByRoleOptions().setName(Texts.get(locale, "register")));
        errorMessage = page.locator(".login-form .alert-danger");
    }

    public LoginPage open(String path) {
        page.navigate(localizedUrl(path));
        return verifyLoaded();
    }

    @Override
    public LoginPage verifyLoaded() {
        AllureArtifacts.screenshot(page, "Login page");
        super.verifyLoaded();
        assertThat(heading).isVisible();
        return this;
    }

    @Override
    public LoginPage checkStructure() {
        AllureArtifacts.screenshot(page, "Checking login page structure");
        super.checkStructure();
        assertThat(heading).isVisible();
        assertThat(emailInput).isVisible();
        assertThat(passwordInput).isVisible();
        assertThat(submitButton).isVisible();
        assertThat(registerLink).isVisible();
        return this;
    }

    public RegistrationPage openRegistrationPage() {
        AllureArtifacts.screenshot(page, "Navigating to registration page");
        registerLink.click();
        return new RegistrationPage(page, locale).verifyLoaded();
    }

    public HomePage login(String email, String password) {
        AllureArtifacts.screenshot(page, "Logging in as " + email);
        submit(email, password);
        return new HomePage(page, locale).verifyLoaded();
    }

    public LoginPage loginWithInvalid(String email, String password) {
        AllureArtifacts.screenshot(page, "Submitting login with email: " + email);
        submit(email, password);
        return this;
    }

    public LoginPage verifyEmailError() {
        // Do not check the message itself: it is browser-dependent, not PrestaShop functionality.
        Boolean validityState = (Boolean) emailInput.evaluate("element => !element.validity.valid");
        assertNotNull(validityState, "Email input validity state should not be null");
        assertTrue(validityState, "No message about invalid email");
        return this;
    }

    public LoginPage verifyErrorMessage(String message) {
        assertThat(errorMessage).isVisible();
        assertThat(errorMessage).hasText(message);
        return this;
    }

    private void submit(String email, String password) {
        emailInput.fill(email);
        passwordInput.fill(password);
        submitButton.click();
    }

    @Override
    public LoginPage verifyCurrentLanguage(String expectedLocale) {
        super.verifyCurrentLanguage(expectedLocale);
        assertThat(heading).isVisible();
        return this;
    }
}
