package com.prestashop.test.base;

public record TestRunConfig(String browser, BrowserProfile profile) {

    @Override
    public String toString() {
        return browser + " / " + profile;
    }
}
