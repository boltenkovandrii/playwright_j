package com.prestashop.test.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class CartPage extends BaseStorefrontPage {

    public CartPage(Page page, String locale) {
        super(page, locale);
    }

    @Override
    public CartPage verifyLoaded() {
        super.verifyLoaded();
        assertThat(page.getByTestId("cart")).isVisible();
        return this;
    }

    @Override
    public CartPage checkStructure() {
        super.checkStructure();
        assertThat(page.locator(".cart-grid")).isVisible();
        assertThat(page.locator(".cart-overview")).isVisible();
        return this;
    }

    public CartPage checkEmptyStructure() {
        super.checkStructure();
        assertThat(page.locator(".no-items")).containsText(Texts.get(locale, "cartEmpty"));
        return this;
    }

    public CartPage verifyProductCount(int count) {
        assertThat(cartItems()).hasCount(count);
        return this;
    }

    public CartPage verifyProductQuantity(ProductSpec product, int quantity) {
        assertThat(item(product).locator("input.js-cart-line-product-quantity")).hasValue(String.valueOf(quantity));
        return this;
    }

    public CartPage setProductQuantity(ProductSpec product, int quantity) {
        Locator input = item(product).locator("input.js-cart-line-product-quantity");
        input.fill(String.valueOf(quantity));
        input.press("Tab");
        assertThat(input).hasValue(String.valueOf(quantity));
        return this;
    }

    public CartPage removeProduct(ProductSpec product) {
        item(product).locator(".remove-from-cart").click();
        assertThat(item(product)).hasCount(0);
        return this;
    }

    public CartPage verifyProductsSubtotal(double amount) {
        assertThat(page.getByTestId("cart-subtotal-products").locator(".value")).hasText("€" + amount);
        return this;
    }

    public CartPage verifyTotal(double amount) {
        assertThat(page.locator(".cart-summary-line.cart-total .value")).hasText("€" + amount);
        return this;
    }

    //TODO: check if needed
    public HomePage continueShopping() {
        page.getByRole(com.microsoft.playwright.options.AriaRole.LINK,
                new Page.GetByRoleOptions().setName(Texts.get(locale, "continue"))).click();
        return new HomePage(page, locale).verifyLoaded();
    }

    public CheckoutPage proceedToCheckout() {
        page.getByRole(com.microsoft.playwright.options.AriaRole.LINK,
                new Page.GetByRoleOptions().setName(Texts.get(locale, "checkout"))).click();
        return new CheckoutPage(page, locale).verifyLoaded();
    }

    private Locator cartItems() {
        return page.locator(".cart-items .cart-item");
    }

    private Locator item(ProductSpec product) {
        Locator candidates = cartItems().filter(new Locator.FilterOptions().setHasText(product.name()));
        for (var attribute : product.attributes().entrySet()) {
            candidates = candidates.filter(new Locator.FilterOptions()
                    .setHas(page.locator("." + attribute.getKey() + " .value")
                            .filter(new Locator.FilterOptions().setHasText(attribute.getValue()))));
        }
        return candidates.first();
    }
}
