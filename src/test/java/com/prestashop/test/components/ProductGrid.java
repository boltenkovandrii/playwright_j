package com.prestashop.test.components;

import com.microsoft.playwright.Locator;
import com.prestashop.test.base.AllureArtifacts;

import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class ProductGrid {

    private final Locator container;
    private final Locator cards;

    public ProductGrid(Locator parent) {
        container = parent.locator(".products.row");
        cards = container.locator(".product-miniature");
    }

    public ProductGrid verifyLoaded() {
        assertThat(container).isVisible();
        return this;
    }

    public ProductGrid checkStructure() {
        AllureArtifacts.screenshot(container, "Checking product grid structure");
        assertThat(container).isVisible();
        assertThat(cards.first()).isVisible();
        productAt(0).checkStructure();
        return this;
    }

    public ProductGrid checkCount(int expectedCount) {
        assertThat(cards).hasCount(expectedCount);
        return this;
    }

    public ProductCard productAt(int index) {
        return new ProductCard(cards.nth(index));
    }

    public List<String> names() {
        return cards.all().stream().map(card -> new ProductCard(card).name()).toList();
    }

    public List<Double> prices() {
        return cards.all().stream().map(card -> new ProductCard(card).price()).toList();
    }

    public ProductGrid verifyNamesContain(String term) {
        for (Locator card : cards.all()) {
            assertThat(card.locator(".product-title a")).containsText(term);
        }
        return this;
    }

    public void openProductByName(String name) {
        cards.filter(new Locator.FilterOptions().setHasText(name)).first().locator(".product-title a").click();
    }
}
