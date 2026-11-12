package com.prestashop.test.pages;

import com.prestashop.test.base.AllureArtifacts;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static com.microsoft.playwright.options.AriaRole.BUTTON;
import static com.microsoft.playwright.options.AriaRole.HEADING;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RegistrationPage extends BaseStorefrontPage {

    private final Locator heading;
    private final Locator socialTitleMr;
    private final Locator socialTitleMrs;
    private final Locator firstNameInput;
    private final Locator lastNameInput;
    private final Locator emailInput;
    private final Locator passwordInput;
    private final Locator birthdayInput;
    private final Locator newsletterCheckbox;
    private final Locator offersCheckbox;
    private final Locator privacyCheckbox;
    private final Locator psgdprCheckbox;
    private final Locator submitButton;
    private final Locator errorMessage;

    public RegistrationPage(Page page, String locale) {
        super(page, locale);
        heading = page.getByRole(HEADING, new Page.GetByRoleOptions().setName(Texts.get(locale, "registration")));
        socialTitleMr = page.getByTestId("field-id_gender-1");
        socialTitleMrs = page.getByTestId("field-id_gender-2");
        firstNameInput = page.getByTestId("field-firstname");
        lastNameInput = page.getByTestId("field-lastname");
        emailInput = page.getByTestId("field-email");
        passwordInput = page.getByTestId("field-password");
        birthdayInput = page.getByTestId("field-birthday");
        newsletterCheckbox = page.locator("input[name='newsletter']");
        offersCheckbox = page.locator("input[name='optin']");
        privacyCheckbox = page.locator("input[name='customer_privacy']");
        psgdprCheckbox = page.locator("input[name='psgdpr']");
        submitButton = page.getByRole(BUTTON, new Page.GetByRoleOptions().setName(Texts.get(locale, "save")));
        errorMessage = page.locator(".alert-danger");
    }

    public RegistrationPage open(String path) {
        page.navigate(localizedUrl(path));
        return verifyLoaded();
    }

    @Override
    public RegistrationPage verifyLoaded() {
        AllureArtifacts.screenshot(page, "Registration page");
        super.verifyLoaded();
        assertThat(heading).isVisible();
        return this;
    }

    @Override
    public RegistrationPage checkStructure() {
        AllureArtifacts.screenshot(page, "Checking registration page structure");
        super.checkStructure();
        assertThat(heading).isVisible();
        assertThat(socialTitleMr).isVisible();
        assertThat(socialTitleMrs).isVisible();
        assertThat(firstNameInput).isVisible();
        assertThat(lastNameInput).isVisible();
        assertThat(emailInput).isVisible();
        assertThat(passwordInput).isVisible();
        assertThat(birthdayInput).isVisible();
        assertThat(newsletterCheckbox).isVisible();
        assertThat(offersCheckbox).isVisible();
        assertThat(privacyCheckbox).isVisible();
        assertThat(psgdprCheckbox).isVisible();
        assertThat(submitButton).isVisible();
        return this;
    }

    public RegistrationPage fillWith(String firstName, String lastName, String email, String password, String birthday) {
        AllureArtifacts.screenshot(page, "Submitting registration form with partial data");
        fillIfPresent(firstNameInput, firstName);
        fillIfPresent(lastNameInput, lastName);
        fillIfPresent(emailInput, email);
        fillIfPresent(passwordInput, password);
        fillIfPresent(birthdayInput, birthday);
        checkIfNeeded(privacyCheckbox);
        checkIfNeeded(psgdprCheckbox);
        return this;
    }

    public BaseStorefrontPage submit() {
        AllureArtifacts.screenshot(page, "Submitting the registration form");
        submitButton.click();
        // Depending on input this either stays on this page with an error or redirects to the home page.
        return new BaseStorefrontPage(page, locale).verifyLoaded();
    }

    public RegistrationPage verifyFirstNameError() {
        // Do not check the message itself: it is browser-dependent, not PrestaShop functionality.
        Boolean validityState = (Boolean) firstNameInput.evaluate("element => !element.validity.valid");
        assertNotNull(validityState, "First name input validity state should not be null");
        assertTrue(validityState, "No message about invalid first name");
        return this;
    }

    public RegistrationPage verifyEmailError() {
        // Do not check the message itself: it is browser-dependent, not PrestaShop functionality.
        Boolean validityState = (Boolean) emailInput.evaluate("element => !element.validity.valid");
        assertNotNull(validityState, "Email input validity state should not be null");
        assertTrue(validityState, "No message about invalid email");
        return this;
    }

    public RegistrationPage verifyPasswordError() {
        // Do not check the message itself: it is browser-dependent, not PrestaShop functionality.
        Boolean validityState = (Boolean) passwordInput.evaluate("element => !element.validity.valid");
        assertNotNull(validityState, "Password input validity state should not be null");
        assertTrue(validityState, "No message about invalid password");
        return this;
    }

    public RegistrationPage verifyErrorMessage(String message) {
        assertThat(errorMessage).isVisible();
        assertThat(errorMessage).hasText(message);
        return this;
    }

    private void fillIfPresent(Locator input, String value) {
        if (value != null && !value.isEmpty()) {
            input.fill(value);
        }
    }

    private void checkIfNeeded(Locator checkbox) {
        if (!checkbox.isChecked()) {
            checkbox.check();
        }
    }

    @Override
    public RegistrationPage verifyCurrentLanguage(String expectedLocale) {
        super.verifyCurrentLanguage(expectedLocale);
        assertThat(heading).isVisible();
        return this;
    }
}
