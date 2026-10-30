package com.prestashop.test.components;

import com.microsoft.playwright.Locator;
import com.prestashop.test.base.AllureArtifacts;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class ProductCard {

    private final Locator container;
    private final Locator productLink;
    private final Locator price;

    ProductCard(Locator container) {
        this.container = container;
        productLink = container.locator(".product-title a");
        price = container.locator(".price");
    }

    public String getName() {
        return productLink.textContent().strip();
    }

    public double getPrice() {
        String text = price.textContent();
        return Double.parseDouble(text.replaceAll("[^\\d,.]", "").replace(',', '.'));
    }

    public ProductCard openProduct() {
        productLink.click();
        return this;
    }

    public ProductCard checkStructure() {
        AllureArtifacts.screenshot(container, "Checking product card structure");
        assertThat(container).isVisible();
        assertThat(productLink).isVisible();
        assertThat(price).isVisible();
        return this;
    }
}
