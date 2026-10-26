package com.prestashop.test.pages;

import com.microsoft.playwright.Page;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class LoginPage extends BaseStorefrontPage {

    public LoginPage(Page page, String locale) {
        super(page, locale);
    }

    @Override
    public LoginPage verifyLoaded() {
        super.verifyLoaded();
        assertThat(page.getByRole(com.microsoft.playwright.options.AriaRole.HEADING,
                new Page.GetByRoleOptions().setName(Texts.get(locale, "login")))).isVisible();
        return this;
    }

    @Override
    public LoginPage checkStructure() {
        super.checkStructure();
        assertThat(page.getByTestId("field-email")).isVisible();
        assertThat(page.getByTestId("field-password")).isVisible();
        return this;
    }

    public RegistrationPage openRegistrationPage() {
        page.getByRole(com.microsoft.playwright.options.AriaRole.LINK,
                new Page.GetByRoleOptions().setName(Texts.get(locale, "register"))).click();
        return new RegistrationPage(page, locale).verifyLoaded();
    }

    public HomePage login(String email, String password) {
        submit(email, password);
        return new HomePage(page, locale).verifyLoaded();
    }

    public LoginPage loginWithInvalid(String email, String password) {
        submit(email, password);
        return this;
    }

    public LoginPage verifyEmailError() {
        assertThat(page.getByTestId("field-email")).hasJSProperty("validity.valid", false);
        return this;
    }

    public LoginPage verifyErrorMessage(String message) {
        assertThat(page.locator(".login-form .alert-danger")).hasText(message);
        return this;
    }

    private void submit(String email, String password) {
        page.getByTestId("field-email").fill(email);
        page.getByTestId("field-password").fill(password);
        page.getByRole(com.microsoft.playwright.options.AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName(Texts.get(locale, "signIn"))).click();
    }
}
