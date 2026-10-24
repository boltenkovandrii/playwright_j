package com.prestashop.test.tests;



import com.microsoft.playwright.Page;

import com.microsoft.playwright.junit.UsePlaywright;
import com.prestashop.test.base.TestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;


@UsePlaywright
@DisplayName("Prestashop")
class PlaywrightSampleTest extends TestBase {


    @Test
    @Tag("regression")
    @DisplayName("Perform search from the main page:")
    void searchFromMainPageTest(Page page) {

        page.navigate("http://localhost:8090");
        System.out.println(page.title());

        getReporting().makeScreenshot(page, "Main page loaded");


    }

    /*

    @Test
    @Tag("regression")
    @DisplayName("Perform search from the main page:")
    void searchFromMainPageTest() {

        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            Page page = browser.newPage();
            page.navigate("https://playwright.dev");
            System.out.println(page.title());
        }
//        Assertions.assertEquals(articlePage.getHeader(), header, "Check header");

    }

     */

}
