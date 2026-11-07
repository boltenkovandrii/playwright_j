package com.prestashop.test.pages;

import com.prestashop.test.base.AllureArtifacts;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static com.microsoft.playwright.options.AriaRole.HEADING;

// This page is almost not localized. Existing scenarios use English summary labels.
public class OrderConfirmationPage extends BaseStorefrontPage {

    private static final double EPSILON = 0.01;

    private final Locator confirmationBlock;
    private final Locator confirmationHeading;
    private final Locator orderItems;
    private final Locator orderLines;
    private final Locator orderSummaryTable;
    private final Locator orderDetails;
    private final Locator orderReference;

    public OrderConfirmationPage(Page page, String locale) {
        super(page, locale);
        confirmationBlock = page.getByTestId("content-hook_order_confirmation");
        confirmationHeading = confirmationBlock.getByRole(HEADING,
                new Locator.GetByRoleOptions().setName(Texts.get(locale, "confirmation")));
        orderItems = page.getByTestId("order-items");
        orderLines = orderItems.locator(".order-line");
        orderSummaryTable = page.locator(".order-confirmation-table table");
        orderDetails = page.getByTestId("order-details");
        orderReference = page.getByTestId("order-reference-value");
    }

    @Override
    public OrderConfirmationPage verifyLoaded() {
        AllureArtifacts.screenshot(page, "Order confirmation page");
        super.verifyLoaded();
        if (!page.url().matches(".*order-confirmation.*")) {
            throw new AssertionError("Expected order confirmation URL but was " + page.url());
        }
        assertThat(confirmationBlock).isVisible();
        return this;
    }

    @Override
    public OrderConfirmationPage checkStructure() {
        AllureArtifacts.screenshot(page, "Checking order confirmation structure");
        assertThat(confirmationBlock).isVisible();
        assertThat(confirmationHeading).isVisible();
        assertThat(orderLines.first()).isVisible();
        assertThat(orderSummaryTable).isVisible();
        assertThat(orderDetails).isVisible();
        return this;
    }

    public OrderConfirmationPage verifyOrderReferencePresent() {
        assertThat(orderReference).containsText("Order reference:");
        return this;
    }

    public OrderConfirmationPage verifyProductPresent(String name) {
        assertThat(orderItems).containsText(name);
        return this;
    }

    public OrderConfirmationPage verifyShippingMethod(String name) {
        assertThat(orderDetails).containsText(name);
        return this;
    }

    public OrderConfirmationPage verifyPaymentMethod(String name) {
        assertThat(orderDetails).containsText(name);
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
        Locator row = orderSummaryTable.locator("tr").filter(new Locator.FilterOptions().setHasText(label));
        String text = row.locator("td").last().textContent().replace("€", "").replace("\u00A0", "").strip();
        if (Math.abs(Double.parseDouble(text) - amount) >= EPSILON) {
            throw new AssertionError("Expected " + amount + " for " + label + " but was " + text);
        }
        return this;
    }

    @Override
    public OrderConfirmationPage verifyCurrentLanguage(String expectedLocale) {
        super.verifyCurrentLanguage(expectedLocale);
        assertThat(confirmationHeading).isVisible();
        return this;
    }
}
