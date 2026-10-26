package com.prestashop.test.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.SelectOption;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class CheckoutPage extends BaseStorefrontPage {

    public CheckoutPage(Page page, String locale) {
        super(page, locale);
    }

    @Override
    public CheckoutPage verifyLoaded() {
        assertThat(page.getByTestId("checkout")).isVisible();
        assertThat(page.getByTestId("checkout-personal-information-step")).isVisible();
        return this;
    }

    @Override
    public CheckoutPage checkStructure() {
        assertThat(page.getByTestId("checkout")).isVisible();
        assertThat(page.getByTestId("checkout-personal-information-step")).isVisible();
        assertThat(page.getByTestId("checkout-addresses-step")).isVisible();
        assertThat(page.getByTestId("checkout-delivery-step")).isVisible();
        assertThat(page.getByTestId("checkout-payment-step")).isVisible();
        return this;
    }

    public CheckoutPage fillPersonalInfo(String firstName, String lastName, String email) {
        Locator form = page.getByTestId("checkout-personal-information-step").getByTestId("customer-form");
        form.getByTestId("field-firstname").fill(firstName);
        form.getByTestId("field-lastname").fill(lastName);
        form.getByTestId("field-email").fill(email);
        checkIfNeeded(form.locator("input[name='customer_privacy']"));
        checkIfNeeded(form.locator("input[name='psgdpr']"));
        form.getByRole(com.microsoft.playwright.options.AriaRole.BUTTON,
                new Locator.GetByRoleOptions().setName(Texts.get(locale, "continueCheckout"))).click();
        return this;
    }

    public CheckoutPage fillAddress(String address, String city, String postcode, String country, String phone, String state) {
        Locator step = page.getByTestId("checkout-addresses-step");
        step.getByTestId("field-address1").fill(address);
        step.getByTestId("field-city").fill(city);
        step.getByTestId("field-postcode").fill(postcode);
        step.getByTestId("field-id_country").selectOption(new SelectOption().setLabel(country));
        if (state != null) {
            step.getByTestId("field-id_state").selectOption(new SelectOption().setLabel(state));
        }
        step.getByTestId("field-phone").fill(phone);
        step.getByRole(com.microsoft.playwright.options.AriaRole.BUTTON,
                new Locator.GetByRoleOptions().setName(Texts.get(locale, "continueCheckout"))).click();
        return this;
    }

    public CheckoutPage selectShippingMethod(String name) {
        Locator option = page.locator(".js-delivery-option").filter(new Locator.FilterOptions().setHasText(name));
        option.locator("input[type='radio']").check();
        page.waitForLoadState();
        page.getByTestId("checkout-delivery-step").getByRole(com.microsoft.playwright.options.AriaRole.BUTTON,
                new Locator.GetByRoleOptions().setName(Texts.get(locale, "continueCheckout"))).click();
        return this;
    }

    public CheckoutPage selectPaymentMethod(String name) {
        page.locator(".payment-option").filter(new Locator.FilterOptions().setHasText(name))
                .locator("input[name='payment-option']").check();
        return this;
    }

    public CheckoutPage acceptTerms() {
        checkIfNeeded(page.locator("input[id='conditions_to_approve[terms-and-conditions]']"));
        return this;
    }

    public OrderConfirmationPage placeOrder() {
        page.getByTestId("checkout-payment-step").getByRole(com.microsoft.playwright.options.AriaRole.BUTTON,
                new Locator.GetByRoleOptions().setName(Texts.get(locale, "placeOrder"))).click();
        return new OrderConfirmationPage(page, locale).verifyLoaded();
    }

    private void checkIfNeeded(Locator locator) {
        if (!locator.isChecked()) {
            locator.check();
        }
    }
}
