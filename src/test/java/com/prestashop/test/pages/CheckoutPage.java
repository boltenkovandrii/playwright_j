package com.prestashop.test.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.SelectOption;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static com.microsoft.playwright.options.AriaRole.BUTTON;
import static com.microsoft.playwright.options.AriaRole.HEADING;

public class CheckoutPage extends BaseStorefrontPage {

    private final Locator checkout;
    private final Locator subtotalProducts;
    private final Locator subtotalShipping;
    private final Locator total;
    private final Locator personalInfoStep;
    private final Locator addressesStep;
    private final Locator shippingStep;
    private final Locator paymentStep;
    private final Locator personalInfoHeading;
    private final Locator addressesHeading;
    private final Locator shippingHeading;
    private final Locator paymentHeading;
    private final Locator personalInfoForm;
    private final Locator firstNameInput;
    private final Locator lastNameInput;
    private final Locator emailInput;
    private final Locator customerPrivacyCheckbox;
    private final Locator psgdprCheckbox;
    private final Locator personalInfoContinueButton;
    private final Locator addressInput;
    private final Locator cityInput;
    private final Locator postcodeInput;
    private final Locator countrySelect;
    private final Locator stateSelect;
    private final Locator phoneInput;
    private final Locator addressContinueButton;
    private final Locator deliveryOptions;
    private final Locator shippingOptionRadios;
    private final Locator shippingContinueButton;
    private final Locator paymentOptions;
    private final Locator paymentOptionRadios;
    private final Locator termsCheckbox;
    private final Locator placeOrderButton;

    public CheckoutPage(Page page, String locale) {
        super(page, locale);
        checkout = page.getByTestId("checkout");
        subtotalProducts = page.getByTestId("cart-subtotal-products");
        subtotalShipping = page.getByTestId("cart-subtotal-shipping");
        total = page.locator(".cart-total");
        personalInfoStep = page.getByTestId("checkout-personal-information-step");
        addressesStep = page.getByTestId("checkout-addresses-step");
        shippingStep = page.getByTestId("checkout-delivery-step");
        paymentStep = page.getByTestId("checkout-payment-step");
        personalInfoHeading = page.getByRole(HEADING,
                new Page.GetByRoleOptions().setName(Texts.get(locale, "checkoutPersonalInformation")));
        addressesHeading = page.getByRole(HEADING,
                new Page.GetByRoleOptions().setName(Texts.get(locale, "checkoutAddresses")));
        shippingHeading = page.getByRole(HEADING,
                new Page.GetByRoleOptions().setName(Texts.get(locale, "checkoutShipping")));
        paymentHeading = page.getByRole(HEADING,
                new Page.GetByRoleOptions().setName(Texts.get(locale, "checkoutPayment")));
        personalInfoForm = personalInfoStep.getByTestId("customer-form");
        firstNameInput = personalInfoForm.getByTestId("field-firstname");
        lastNameInput = personalInfoForm.getByTestId("field-lastname");
        emailInput = personalInfoForm.getByTestId("field-email");
        customerPrivacyCheckbox = personalInfoForm.locator("input[name='customer_privacy']");
        psgdprCheckbox = personalInfoForm.locator("input[name='psgdpr']");
        personalInfoContinueButton = personalInfoForm.getByRole(BUTTON,
                new Locator.GetByRoleOptions().setName(Texts.get(locale, "continueCheckout")));
        addressInput = addressesStep.getByTestId("field-address1");
        cityInput = addressesStep.getByTestId("field-city");
        postcodeInput = addressesStep.getByTestId("field-postcode");
        countrySelect = addressesStep.getByTestId("field-id_country");
        stateSelect = addressesStep.getByTestId("field-id_state");
        phoneInput = addressesStep.getByTestId("field-phone");
        addressContinueButton = addressesStep.getByRole(BUTTON,
                new Locator.GetByRoleOptions().setName(Texts.get(locale, "continueCheckout")));
        deliveryOptions = shippingStep.locator(".js-delivery-option");
        shippingOptionRadios = shippingStep.locator("input[type='radio'][name^='delivery_option']");
        shippingContinueButton = shippingStep.getByRole(BUTTON,
                new Locator.GetByRoleOptions().setName(Texts.get(locale, "continueCheckout")));
        paymentOptions = paymentStep.locator(".payment-option");
        paymentOptionRadios = paymentStep.locator("input[name='payment-option']");
        termsCheckbox = paymentStep.locator("input[id='conditions_to_approve[terms-and-conditions]']");
        placeOrderButton = paymentStep.getByRole(BUTTON,
                new Locator.GetByRoleOptions().setName(Texts.get(locale, "placeOrder")));
    }

    @Override
    public CheckoutPage verifyLoaded() {
        assertThat(checkout).isVisible();
        assertThat(personalInfoStep).isVisible();
        return this;
    }

    @Override
    public CheckoutPage checkStructure() {
        assertThat(checkout).isVisible();
        assertThat(subtotalProducts).isVisible();
        assertThat(subtotalShipping).isVisible();
        assertThat(total).isVisible();
        assertThat(personalInfoStep).isVisible();
        assertThat(addressesStep).isVisible();
        assertThat(shippingStep).isVisible();
        assertThat(paymentStep).isVisible();
        return this;
    }

    public CheckoutPage checkPersonalInfoStructure() {
        assertThat(personalInfoStep).isVisible();
        assertThat(personalInfoHeading).isVisible();
        assertThat(personalInfoForm).isVisible();
        assertThat(firstNameInput).isVisible();
        assertThat(lastNameInput).isVisible();
        assertThat(emailInput).isVisible();
        assertThat(personalInfoContinueButton).isVisible();
        return this;
    }

    public CheckoutPage fillPersonalInfo(String firstName, String lastName, String email) {
        firstNameInput.fill(firstName);
        lastNameInput.fill(lastName);
        emailInput.fill(email);
        checkIfNeeded(customerPrivacyCheckbox);
        checkIfNeeded(psgdprCheckbox);
        personalInfoContinueButton.click();
        assertThat(addressesStep).hasAttribute("class", Pattern.compile("js-current-step"));
        return this;
    }

    public CheckoutPage checkAddressesStructure() {
        assertThat(addressesStep).isVisible();
        assertThat(addressesHeading).isVisible();
        assertThat(addressInput).isVisible();
        assertThat(cityInput).isVisible();
        assertThat(postcodeInput).isVisible();
        assertThat(countrySelect).isVisible();
        assertThat(addressContinueButton).isVisible();
        return this;
    }

    public CheckoutPage fillAddress(String address, String city, String postcode, String country, String phone, String state) {
        addressInput.fill(address);
        cityInput.fill(city);
        postcodeInput.fill(postcode);
        countrySelect.selectOption(new SelectOption().setLabel(country));
        if (state != null) {
            stateSelect.selectOption(new SelectOption().setLabel(state));
        } else {
            assertThat(stateSelect).not().isVisible();
        }
        phoneInput.fill(phone);
        addressContinueButton.click();
        assertThat(shippingStep).hasAttribute("class", Pattern.compile("js-current-step"));
        return this;
    }

    public CheckoutPage checkShippingMethodStructure() {
        assertThat(shippingStep).isVisible();
        assertThat(shippingHeading).isVisible();
        assertThat(shippingOptionRadios.first()).isVisible();
        assertThat(shippingContinueButton).isVisible();
        return this;
    }

    public CheckoutPage selectShippingMethod(String name) {
        Locator option = deliveryOptions.filter(new Locator.FilterOptions().setHasText(name));
        option.locator("input[type='radio']").check();
        page.waitForLoadState();
        shippingContinueButton.click();
        return this;
    }

    public CheckoutPage checkPaymentStructure() {
        assertThat(paymentStep).isVisible();
        assertThat(paymentHeading).isVisible();
        assertThat(paymentOptionRadios.first()).isVisible();
        assertThat(termsCheckbox).isVisible();
        assertThat(placeOrderButton).isVisible();
        return this;
    }

    public CheckoutPage selectPaymentMethod(String name) {
        Locator paymentRadio = paymentOptions.filter(new Locator.FilterOptions().setHasText(name))
                .locator("input[type='radio'][name='payment-option']");
        paymentRadio.check();
        assertThat(paymentRadio).isChecked();
        return this;
    }

    public CheckoutPage acceptTerms() {
        checkIfNeeded(termsCheckbox);
        assertThat(termsCheckbox).isChecked();
        return this;
    }

    public OrderConfirmationPage placeOrder() {
        placeOrderButton.click();
        return new OrderConfirmationPage(page, locale).verifyLoaded();
    }

    private void checkIfNeeded(Locator locator) {
        if (!locator.isChecked()) {
            locator.check();
        }
    }
}
