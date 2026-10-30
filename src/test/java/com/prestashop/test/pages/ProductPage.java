package com.prestashop.test.pages;

import com.prestashop.test.base.AllureArtifacts;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.SelectOption;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static com.microsoft.playwright.options.AriaRole.BUTTON;
import static com.microsoft.playwright.options.AriaRole.LINK;
import static com.microsoft.playwright.options.AriaRole.TAB;

public class ProductPage extends BaseStorefrontPage {

    private final Locator product;
    private final Locator productName;
    private final Locator productInformation;
    private final Locator coverImage;
    private final Locator price;
    private final Locator regularPrice;
    private final Locator shortDescription;
    private final Locator descriptionTab;
    private final Locator descriptionPanel;
    private final Locator fullDescription;
    private final Locator detailsTab;
    private final Locator detailsPanel;
    private final Locator inStock;
    private final Locator addToCartButton;
    private final Locator cartModal;
    private final Locator modalTitle;
    private final Locator modalProductName;
    private final Locator modalProductQuantity;
    private final Locator modalItemCount;
    private final Locator modalTotal;
    private final Locator modalSubtotal;
    private final Locator continueShoppingButton;
    private final Locator proceedToCartLink;
    private final Locator quantityInput;
    private final Locator quantityIncreaseButton;
    private final Locator quantityDecreaseButton;
    private final Locator sizeSelect;
    private final Locator sizeOptions;
    private final Locator selectedSize;
    private final Locator colorOptions;
    private final Locator breadcrumbLinks;

    public ProductPage(Page page, String locale) {
        super(page, locale);
        product = page.getByTestId("main");
        productName = product.locator("h1");
        productInformation = page.locator(".product-information");
        coverImage = page.locator(".product-cover .img-fluid");
        price = page.locator(".current-price .current-price-value");
        regularPrice = page.locator(".product-discount .regular-price");
        shortDescription = page.locator("div[id*='product-description-short-']");
        descriptionTab = page.getByRole(TAB, new Page.GetByRoleOptions().setName(Texts.get(locale, "description")));
        descriptionPanel = page.getByTestId("description");
        fullDescription = page.locator(".tab-content .product-description");
        detailsTab = page.getByRole(TAB, new Page.GetByRoleOptions().setName(Texts.get(locale, "details")));
        detailsPanel = page.getByTestId("product-details");
        inStock = page.locator(".product-quantities span");
        addToCartButton = page.locator("[data-button-action='add-to-cart']");
        cartModal = page.getByTestId("blockcart-modal");
        modalTitle = cartModal.locator(".modal-title");
        modalProductName = cartModal.locator(".product-name");
        modalProductQuantity = cartModal.locator(".product-quantity strong");
        modalItemCount = cartModal.locator(".cart-products-count");
        modalTotal = cartModal.locator(".product-total .value");
        modalSubtotal = cartModal.locator(".subtotal.value");
        continueShoppingButton = page.getByRole(BUTTON, new Page.GetByRoleOptions().setName(Texts.get(locale, "continue")));
        proceedToCartLink = page.getByRole(LINK, new Page.GetByRoleOptions().setName(Texts.get(locale, "checkout")));
        quantityInput = page.getByTestId("quantity_wanted");
        quantityIncreaseButton = page.locator(".bootstrap-touchspin-up");
        quantityDecreaseButton = page.locator(".bootstrap-touchspin-down");
        sizeSelect = page.getByLabel(Texts.get(locale, "size"));
        sizeOptions = sizeSelect.locator("option");
        selectedSize = sizeSelect.locator("option:checked");
        colorOptions = page.locator(".input-color");
        breadcrumbLinks = page.locator("nav.breadcrumb a");
    }

    @Override
    public ProductPage verifyLoaded() {
        AllureArtifacts.screenshot(page, "Product page");
        super.verifyLoaded();
        assertThat(product).isVisible();
        return this;
    }

    @Override
    public ProductPage checkStructure() {
        AllureArtifacts.screenshot(page, "Checking product page structure");
        super.checkStructure();
        assertThat(coverImage).isVisible();
        assertThat(productInformation).isVisible();
        assertThat(shortDescription).isVisible();
        assertThat(descriptionTab).isVisible();
        assertThat(detailsTab).isVisible();
        assertThat(addToCartButton).isVisible();
        return this;
    }

    public ProductPage verifyProductName(String name) {
        AllureArtifacts.screenshot(page, "Verifying product name");
        assertThat(productName).containsText(name);
        return this;
    }

    public ProductPage verifyPrice(double amount) {
        assertThat(price).hasText("€" + amount);
        return this;
    }

    public ProductPage verifyRegularPrice(double amount) {
        assertThat(regularPrice).hasText("€" + amount);
        return this;
    }

    public ProductPage verifyShortDescription(String text) {
        assertThat(shortDescription).containsText(text);
        return this;
    }

    public ProductPage verifyFullDescription(String text) {
        AllureArtifacts.screenshot(page, "Verifying full description");
        descriptionTab.click();
        assertThat(descriptionPanel).isVisible();
        assertThat(fullDescription).containsText(text);
        return this;
    }

    public ProductPage verifyInStockCount(int count) {
        AllureArtifacts.screenshot(page, "Verifying stock count");
        detailsTab.click();
        assertThat(detailsPanel).isVisible();
        assertThat(inStock).hasText(count + " Items");
        return this;
    }

    public ProductPage increaseQuantity() {
        quantityIncreaseButton.click();
        return this;
    }

    public ProductPage decreaseQuantity() {
        quantityDecreaseButton.click();
        return this;
    }

    public ProductPage setQuantity(int value) {
        quantityInput.fill(String.valueOf(value));
        quantityInput.press("Tab");
        return this;
    }

    public ProductPage verifyQuantity(int quantity) {
        assertThat(quantityInput).hasValue(String.valueOf(quantity));
        return this;
    }

    public ProductPage verifySizeOptions(String... expectedSizes) {
        assertThat(sizeOptions).hasCount(expectedSizes.length);
        for (int index = 0; index < expectedSizes.length; index++) {
            assertThat(sizeOptions.nth(index)).hasText(expectedSizes[index]);
        }
        return this;
    }

    public ProductPage verifyColorOptionCount(int count) {
        assertThat(colorOptions.first()).isVisible();
        assertThat(colorOptions).hasCount(count);
        return this;
    }

    public ProductPage selectSize(String size) {
        AllureArtifacts.screenshot(page, "Selecting size: " + size);
        // Need to wait until the request is processed, otherwise subsequent actions can use stale page state.
        page.waitForResponse(resp -> resp.url().contains("controller=product") && resp.status() == 200,
                () ->  sizeSelect.selectOption(new SelectOption().setLabel(size))
        );
        assertThat(selectedSize).hasText(size);
        return this;
    }

    public ProductPage verifySelectedSize(String size) {
        assertThat(selectedSize).hasText(size);
        return this;
    }

    public ProductPage selectColor(String color) {
        Locator option = colorOption(color);
        if (!option.isChecked()) {
            AllureArtifacts.screenshot(page, "Selecting color: " + color);
            String original = imageSource();
            option.check(new Locator.CheckOptions().setForce(true));
            assertThat(option).isChecked();
            assertThat(coverImage).not().hasAttribute("src", original);
        } else {
            AllureArtifacts.screenshot(page, "Color is already selected: " + color);
        }
        return this;
    }

    public ProductPage verifySelectedColor(String color) {
        assertThat(colorOption(color)).isChecked();
        return this;
    }

    public ProductPage verifyProductContextVisible() {
        assertThat(productName).isVisible();
        assertThat(addToCartButton).isVisible();
        return this;
    }

    public String imageSource() {
        String source = coverImage.getAttribute("src");
        if (source == null) {
            throw new AssertionError("Product image source is not available");
        }
        return source;
    }

    public ProductPage addToCart() {
        AllureArtifacts.screenshot(page, "Adding product to cart");
        addToCartButton.click();
        assertThat(cartModal).isVisible();
        AllureArtifacts.screenshot(page, "After adding item to cart");
        return this;
    }

    public ProductPage verifyAddToCartConfirmation(String name, int quantity, double total, double subtotal) {
        assertThat(modalTitle).containsText(Texts.get(locale, "productAdded"));
        assertThat(modalProductName).hasText(name);
        assertThat(modalProductQuantity).hasText(String.valueOf(quantity));
        assertThat(modalItemCount).hasText(cartItemCountText(quantity));
        assertThat(modalTotal).hasText("€" + total);
        assertThat(modalSubtotal).hasText("€" + subtotal);
        assertThat(continueShoppingButton).isVisible();
        assertThat(proceedToCartLink).isVisible();
        return this;
    }

    public ProductPage continueShopping() {
        AllureArtifacts.screenshot(page, "Continuing shopping from cart confirmation");
        continueShoppingButton.click();
        assertThat(cartModal).isHidden();
        return this;
    }

    public ProductPage verifyAddToCartConfirmationClosed() {
        assertThat(cartModal).isHidden();
        return this;
    }

    @Override
    public ProductPage verifyHeaderCartCount(int count) {
        header.verifyCartCount(count);
        return this;
    }

    public CartPage proceedToCart() {
        AllureArtifacts.screenshot(page, "Going to cart");
        proceedToCartLink.click();
        return new CartPage(page, locale).verifyLoaded();
    }

    public CatalogPage goToCategoryFromBreadcrumb(String name) {
        AllureArtifacts.screenshot(page, "Going to category from breadcrumb");
        breadcrumbLinks.filter(new Locator.FilterOptions().setHasText(name)).click();
        return new CatalogPage(page, locale).verifyLoaded();
    }

    private Locator colorOption(String color) {
        return page.locator(".input-color[title='" + color + "'], .input-color[aria-label='" + color + "']");
    }

    private String cartItemCountText(int count) {
        if (count == 1) {
            return Texts.get(locale, "cartItemsOne");
        }
        return Texts.get(locale, "cartItemsMany").replace("{count}", String.valueOf(count));
    }

    @Override
    public ProductPage verifyCurrentLanguage(String expectedLocale) {
        super.verifyCurrentLanguage(expectedLocale);
        assertThat(descriptionTab).isVisible();
        assertThat(detailsTab).isVisible();
        assertThat(sizeSelect).isVisible();
        return this;
    }
}
