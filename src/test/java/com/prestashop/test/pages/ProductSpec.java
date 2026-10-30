package com.prestashop.test.pages;

import java.util.Map;

public record ProductSpec(String name, Map<String, String> attributes) {

    public ProductSpec(String name) {
        this(name, Map.of());
    }
}
