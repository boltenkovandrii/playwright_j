package com.prestashop.test.pages;

import com.microsoft.playwright.Page;
import com.prestashop.test.base.TestConfig;

public abstract class BasePage<T extends BasePage<T>> {

    public static final int BOOTSTRAP_MD = 768;

    public static final int TIMEOUT = 1000;

    protected final Page page;

    protected BasePage(Page page) {
        this.page = page;
    }

    public abstract T verifyLoaded();

    public static String baseUrl() {
        return TestConfig.getConfiguration().getString("baseUrlPrestaShop", "http://localhost:8090").replaceAll("/+$", "");
    }
}
