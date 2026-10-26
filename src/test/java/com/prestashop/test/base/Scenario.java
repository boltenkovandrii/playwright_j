package com.prestashop.test.base;

import com.microsoft.playwright.Page;

@FunctionalInterface
public interface Scenario {
    void execute(Page page) throws Exception;
}
