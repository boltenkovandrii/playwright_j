package com.prestashop.test.pages;

import com.prestashop.test.base.AllureArtifacts;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static com.microsoft.playwright.options.AriaRole.LINK;

public class CartPage extends BaseStorefrontPage {
    private final Locator cart;
    private final Locator cartOverview;
    private final Locator detailedTotals;
    private final Locator cartItems;
    private final Locator productNames;
    private final Locator productsSubtotal;
    private final Locator total;
    private final Locator emptyMessage;
    private final Locator continueShoppingLink;
    private final Locator proceedToCheckoutLink;

    public CartPage(Page page, String locale) {
        super(page, locale);
        cart = page.locator(".cart-grid");
        cartOverview = page.locator(".cart-overview");
        detailedTotals = page.locator(".cart-detailed-totals");
        cartItems = page.locator(".cart-items .cart-item");
        productNames = cartItems.locator(".product-line-info a.label");
        productsSubtotal = page.getByTestId("cart-subtotal-products").locator(".value");
        total = page.locator(".cart-summary-line.cart-total .value");
        emptyMessage = page.locator(".no-items");
        continueShoppingLink = page.getByRole(LINK, new Page.GetByRoleOptions().setName(Texts.get(locale, "continue")));
        proceedToCheckoutLink = page.getByRole(LINK, new Page.GetByRoleOptions().setName(Texts.get(locale, "checkout")));
    }

    @Override
    public CartPage verifyLoaded() {
        AllureArtifacts.screenshot(page, "Cart page");
        super.verifyLoaded();
        assertThat(page.getByTestId("cart")).isVisible();
        return this;
    }

    @Override
    public CartPage checkStructure() {
        AllureArtifacts.screenshot(page, "Checking cart page structure");
        super.checkStructure();
        assertThat(cart).isVisible();
        assertThat(cartOverview).isVisible();
        assertThat(detailedTotals).isVisible();
        assertThat(continueShoppingLink).isVisible();
        assertThat(proceedToCheckoutLink).isVisible();
        return this;
    }

    public CartPage checkEmptyStructure() {
        AllureArtifacts.screenshot(page, "Checking empty cart structure");
        super.checkStructure();
        assertThat(emptyMessage).isVisible();
        assertThat(emptyMessage).containsText(Texts.get(locale, "cartEmpty"));
        return this;
    }

    public CartPage verifyProductCount(int count) {
        assertThat(cartItems).hasCount(count);
        return this;
    }

    public CartPage verifyProductWithNamePresent(String name, int count) {
        Locator matches = productNames.filter(new Locator.FilterOptions().setHasText(name));
        assertThat(matches).hasCount(count);
        assertThat(matches.first()).isVisible();
        return this;
    }

    public CartPage verifyProductPrice(String name, double price) {
        Locator productItem = items(new ProductSpec(name));
        assertThat(productItem).hasCount(1);
        assertThat(productItem.locator(".current-price .price")).hasText("€" + price);
        return this;
    }

    public CartPage verifyProductQuantity(ProductSpec product, int quantity) {
        Locator productItem = items(product);
        assertThat(productItem).hasCount(1);
        assertThat(productItem.locator("input.js-cart-line-product-quantity")).hasValue(String.valueOf(quantity));
        return this;
    }

    public CartPage setProductQuantity(ProductSpec product, int quantity) {
        AllureArtifacts.screenshot(page, "Updating quantity for " + product + " to " + quantity);
        Locator productItem = items(product);
        assertThat(productItem).hasCount(1);
        Locator input = productItem.locator("input.js-cart-line-product-quantity");
        input.fill(String.valueOf(quantity));
        input.press("Tab");
        assertThat(input).hasValue(String.valueOf(quantity));
        return this;
    }

    public CartPage removeProduct(ProductSpec product) {
        AllureArtifacts.screenshot(page, "Removing product from cart: " + product);
        Locator productItem = items(product);
        assertThat(productItem).hasCount(1);
        productItem.locator(".remove-from-cart").click();
        assertThat(items(product)).hasCount(0);
        return this;
    }

    public CartPage verifyProductsSubtotal(double amount) {
        assertThat(productsSubtotal).hasText("€" + amount);
        return this;
    }

    public CartPage verifyTotal(double amount) {
        assertThat(total).hasText("€" + amount);
        return this;
    }

    public HomePage continueShopping() {
        AllureArtifacts.screenshot(page, "Continuing shopping from cart page");
        continueShoppingLink.click();
        return new HomePage(page, locale).verifyLoaded();
    }

    public CheckoutPage proceedToCheckout() {
        AllureArtifacts.screenshot(page, "Proceeding from cart to checkout");
        proceedToCheckoutLink.click();
        return new CheckoutPage(page, locale).verifyLoaded();
    }

    @Override
    public CartPage verifyCurrentLanguage(String expectedLocale) {
        super.verifyCurrentLanguage(expectedLocale);
        assertThat(continueShoppingLink).isVisible();
        assertThat(proceedToCheckoutLink).isVisible();
        return this;
    }

    private Locator items(ProductSpec product) {
        Locator candidates = cartItems.filter(new Locator.FilterOptions().setHasText(product.name()));

        for (var attribute : product.attributes().entrySet()) {
            Locator attributeValue = page.locator("." + attribute.getKey() + " .value").filter(new Locator.FilterOptions().setHasText(attribute.getValue()));
            candidates = candidates.filter(new Locator.FilterOptions().setHas(attributeValue));
        }

        return candidates;
    }
}
