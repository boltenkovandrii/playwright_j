package com.andrii.test.tests;

import com.andrii.test.base.TestBase;
import com.andrii.test.base.TestConfig;
import com.andrii.test.pages.ArticlePage;
import com.andrii.test.pages.MainPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;


@DisplayName("Link popup suite")
class CheckPopupTest extends TestBase {

    @Test
    @Tag("regression")
    @DisplayName("Open link popup")
    void checkPopupTest() {

        getData().getDriver().navigate().to(TestConfig.getConfiguration().getString("baseUrlWiki"));
        ArticlePage articlePage = new MainPage(getData())
                .selectSearchLanguage("English")
                .performSearch("Lviv")
                .hoverOverSuitableLink("Armenian Cathedral");

        //checking popup for first suitable link
        Assertions.assertTrue(articlePage.checkPopupIsVisible(true, true), "Verifying that popup is visible");




    }
}
