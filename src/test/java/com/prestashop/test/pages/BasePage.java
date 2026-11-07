package com.prestashop.test.pages;

import com.microsoft.playwright.Page;

public abstract class BasePage<T extends BasePage<T>> {

    public static final int BOOTSTRAP_MD = 768;

    public static final int TIMEOUT = 1000;

    protected final Page page;

    protected BasePage(Page page) {
        this.page = page;
    }

    public abstract T verifyLoaded();
}
