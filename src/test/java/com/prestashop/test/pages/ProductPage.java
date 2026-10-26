package com.prestashop.test.pages;

import com.prestashop.test.base.AllureArtifacts;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.SelectOption;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class ProductPage extends BaseStorefrontPage {

    public ProductPage(Page page, String locale) {
        super(page, locale);
    }

    @Override
    public ProductPage verifyLoaded() {
        super.verifyLoaded();
        assertThat(page.getByTestId("main").locator("h1")).isVisible();
        return this;
    }

    @Override
    public ProductPage checkStructure() {
        super.checkStructure();
        assertThat(page.locator(".product-cover .img-fluid")).isVisible();
        assertThat(page.locator(".product-information")).isVisible();
        assertThat(page.locator("[data-button-action='add-to-cart']")).isVisible();
        return this;
    }

    public ProductPage verifyProductName(String name) {
        assertThat(page.getByTestId("main").locator("h1")).containsText(name);
        return this;
    }

    public ProductPage verifyPrice(double amount) {
        assertThat(page.locator(".current-price .current-price-value")).hasText("€" + amount);
        return this;
    }

    public ProductPage verifyRegularPrice(double amount) {
        assertThat(page.locator(".product-discount .regular-price")).hasText("€" + amount);
        return this;
    }

    public ProductPage verifyShortDescription(String text) {
        assertThat(page.locator("div[id*='product-description-short-'] p")).containsText(text);
        return this;
    }

    public ProductPage verifyFullDescription(String text) {
        page.getByRole(com.microsoft.playwright.options.AriaRole.TAB,
                new Page.GetByRoleOptions().setName(Texts.get(locale, "description"))).click();
        assertThat(page.locator(".tab-content .product-description")).containsText(text);
        return this;
    }

    public ProductPage verifyInStockCount(int count) {
        page.getByRole(com.microsoft.playwright.options.AriaRole.TAB,
                new Page.GetByRoleOptions().setName(Texts.get(locale, "details"))).click();
        assertThat(page.locator(".product-quantities span")).hasText(count + " Items");
        return this;
    }

    public ProductPage increaseQuantity() {
        page.locator(".bootstrap-touchspin-up").click();
        return this;
    }

    public ProductPage decreaseQuantity() {
        page.locator(".bootstrap-touchspin-down").click();
        return this;
    }

    public ProductPage setQuantity(int value) {
        Locator quantity = page.getByTestId("quantity_wanted");
        quantity.fill(String.valueOf(value));
        quantity.press("Tab");
        return this;
    }

    public ProductPage verifyQuantity(int expectedQuantity) {
        assertThat(page.getByTestId("quantity_wanted")).hasValue(String.valueOf(expectedQuantity));
        return this;
    }

    public ProductPage verifySizeOptions(String... expectedSizes) {
        Locator options = page.getByLabel(Texts.get(locale, "size")).locator("option");
        assertThat(options).hasCount(expectedSizes.length);
        return this;
    }

    public ProductPage verifyColorOptionCount(int count) {
        assertThat(page.locator(".input-color")).hasCount(count);
        return this;
    }

    public ProductPage selectSize(String size) {
        page.getByLabel(Texts.get(locale, "size")).selectOption(new SelectOption().setLabel(size));
        return this;
    }

    public ProductPage verifySelectedSize(String size) {
        assertThat(page.getByLabel(Texts.get(locale, "size")).locator("option:checked")).hasText(size);
        return this;
    }

    public ProductPage selectColor(String color) {
        Locator option = colorOption(color);
        if (!option.isChecked()) {
            String original = imageSource();
            option.check(new Locator.CheckOptions().setForce(true));
            assertThat(page.locator(".product-cover .img-fluid")).not().hasAttribute("src", original);
        }
        return this;
    }

    public ProductPage verifySelectedColor(String color) {
        assertThat(colorOption(color)).isChecked();
        return this;
    }

    public String imageSource() {
        String source = page.locator(".product-cover .img-fluid").getAttribute("src");
        if (source == null) {
            throw new AssertionError("Product image source is not available");
        }
        return source;
    }

    public ProductPage addToCart() {
        AllureArtifacts.screenshot(page, "Adding product to cart");
        page.locator("[data-button-action='add-to-cart']").click();
        assertThat(page.getByTestId("blockcart-modal")).isVisible();
        return this;
    }

    public ProductPage verifyAddToCartConfirmation(String productName, int quantity, double total, double subtotal) {
        Locator modal = page.getByTestId("blockcart-modal");
        assertThat(modal.locator(".product-name")).hasText(productName);
        assertThat(modal.locator(".product-quantity strong")).hasText(String.valueOf(quantity));
        assertThat(modal.locator(".product-total .value")).hasText("€" + total);
        assertThat(modal.locator(".subtotal.value")).hasText("€" + subtotal);
        return this;
    }

    public ProductPage continueShopping() {
        page.getByRole(com.microsoft.playwright.options.AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName(Texts.get(locale, "continue"))).click();
        assertThat(page.getByTestId("blockcart-modal")).isHidden();
        return this;
    }

    public CartPage proceedToCart() {
        page.getByRole(com.microsoft.playwright.options.AriaRole.LINK,
                new Page.GetByRoleOptions().setName(Texts.get(locale, "checkout"))).click();
        return new CartPage(page, locale).verifyLoaded();
    }

    public CatalogPage goToCategoryFromBreadcrumb(String name) {
        page.locator("nav.breadcrumb a").filter(new Locator.FilterOptions().setHasText(name)).click();
        return new CatalogPage(page, locale).verifyLoaded();
    }

    private Locator colorOption(String color) {
        return page.locator(".input-color[title='" + color + "'], .input-color[aria-label='" + color + "']");
    }
}
