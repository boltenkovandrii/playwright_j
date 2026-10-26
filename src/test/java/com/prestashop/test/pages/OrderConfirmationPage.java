package com.prestashop.test.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class OrderConfirmationPage extends BaseStorefrontPage {

    public OrderConfirmationPage(Page page, String locale) {
        super(page, locale);
    }

    @Override
    public OrderConfirmationPage verifyLoaded() {
        assertThat(page.getByTestId("content-hook_order_confirmation")).isVisible();
        return this;
    }

    @Override
    public OrderConfirmationPage checkStructure() {
        assertThat(page.getByTestId("order-items").locator(".order-line").first()).isVisible();
        assertThat(page.locator(".order-confirmation-table table")).isVisible();
        return this;
    }

    public OrderConfirmationPage verifyOrderReferencePresent() {
        assertThat(page.getByTestId("order-reference-value")).containsText("Order reference:");
        return this;
    }

    public OrderConfirmationPage verifyProductPresent(String name) {
        assertThat(page.getByTestId("order-items")).containsText(name);
        return this;
    }

    public OrderConfirmationPage verifyShippingMethod(String name) {
        assertThat(page.getByTestId("order-details")).containsText(name);
        return this;
    }

    public OrderConfirmationPage verifyPaymentMethod(String name) {
        assertThat(page.getByTestId("order-details")).containsText(name);
        return this;
    }

    public OrderConfirmationPage verifyTotal(double amount) {
        return verifySummary("Total (tax incl.)", amount);
    }

    public OrderConfirmationPage verifySubtotal(double amount) {
        return verifySummary("Subtotal", amount);
    }

    public OrderConfirmationPage verifyShipping(double amount) {
        return verifySummary("Shipping and handling", amount);
    }

    private OrderConfirmationPage verifySummary(String label, double amount) {
        Locator row = page.locator(".order-confirmation-table tr").filter(new Locator.FilterOptions().setHasText(label));
        String text = row.locator("td").last().textContent().replace("€", "").replace("\u00A0", "").strip();
        if (Math.abs(Double.parseDouble(text) - amount) >= 0.01) {
            throw new AssertionError("Expected " + amount + " for " + label + " but was " + text);
        }
        return this;
    }
}
