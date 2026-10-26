package com.prestashop.test.components;

import com.microsoft.playwright.Locator;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class ProductCard {

    private final Locator container;

    ProductCard(Locator container) {
        this.container = container;
    }

    public String name() {
        return container.locator(".product-title a").textContent().strip();
    }

    public double price() {
        String text = container.locator(".price").textContent();
        return Double.parseDouble(text.replaceAll("[^\\d,.]", "").replace(',', '.'));
    }

    public ProductCard openProduct() {
        container.locator(".product-title a").click();
        return this;
    }

    public ProductCard checkStructure() {
        assertThat(container).isVisible();
        assertThat(container.locator(".product-title a")).isVisible();
        assertThat(container.locator(".price")).isVisible();
        return this;
    }
}
