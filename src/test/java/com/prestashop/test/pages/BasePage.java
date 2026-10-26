package com.prestashop.test.pages;

import com.microsoft.playwright.Page;

public abstract class BasePage<T extends BasePage<T>> {

    public static int BOOTSTRAP_LG = 992;

    protected final Page page;

    protected BasePage(Page page) {
        this.page = page;
    }

    public abstract T verifyLoaded();
}
