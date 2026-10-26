package com.prestashop.test.pages;

import com.microsoft.playwright.Page;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class RegistrationPage extends BaseStorefrontPage {

    public RegistrationPage(Page page, String locale) {
        super(page, locale);
    }

    @Override
    public RegistrationPage verifyLoaded() {
        super.verifyLoaded();
        assertThat(page.getByRole(com.microsoft.playwright.options.AriaRole.HEADING,
                new Page.GetByRoleOptions().setName(Texts.get(locale, "registration")))).isVisible();
        return this;
    }

    @Override
    public RegistrationPage checkStructure() {
        super.checkStructure();
        assertThat(page.getByTestId("field-firstname")).isVisible();
        assertThat(page.getByTestId("field-lastname")).isVisible();
        assertThat(page.getByTestId("field-email")).isVisible();
        assertThat(page.getByTestId("field-password")).isVisible();
        return this;
    }

    public RegistrationPage fillWith(String firstName, String lastName, String email, String password, String birthday) {
        fillIfPresent("field-firstname", firstName);
        fillIfPresent("field-lastname", lastName);
        fillIfPresent("field-email", email);
        fillIfPresent("field-password", password);
        fillIfPresent("field-birthday", birthday);
        checkIfNeeded("input[name='customer_privacy']");
        checkIfNeeded("input[name='psgdpr']");
        return this;
    }

    public BaseStorefrontPage submit() {
        page.getByRole(com.microsoft.playwright.options.AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName(Texts.get(locale, "save"))).click();
        return new BaseStorefrontPage(page, locale).verifyLoaded();
    }

    public RegistrationPage verifyFirstNameError() {
        assertThat(page.getByTestId("field-firstname")).hasJSProperty("validity.valid", false);
        return this;
    }

    public RegistrationPage verifyEmailError() {
        assertThat(page.getByTestId("field-email")).hasJSProperty("validity.valid", false);
        return this;
    }

    public RegistrationPage verifyPasswordError() {
        assertThat(page.getByTestId("field-password")).hasJSProperty("validity.valid", false);
        return this;
    }

    public RegistrationPage verifyErrorMessage(String message) {
        assertThat(page.locator(".alert-danger")).hasText(message);
        return this;
    }

    private void fillIfPresent(String testId, String value) {
        if (value != null && !value.isEmpty()) {
            page.getByTestId(testId).fill(value);
        }
    }

    private void checkIfNeeded(String selector) {
        if (!page.locator(selector).isChecked()) {
            page.locator(selector).check();
        }
    }
}
