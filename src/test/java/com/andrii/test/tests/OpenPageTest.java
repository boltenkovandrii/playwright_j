package com.andrii.test.tests;

import com.andrii.test.base.TestBase;
import com.andrii.test.base.TestConfig;
import com.andrii.test.pages.ArticlePage;
import com.andrii.test.pages.MainPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

@DisplayName("Open page suite")
class OpenPageTest extends TestBase {

    static Stream<Arguments> dataProvider() {
        return Stream.of(
                arguments("English", "Lviv", "Lviv", "Correct search in English"),
                arguments("Nederlands", "Lviv", "Lviv", "Correct search in Dutch"),
                arguments("English", "Lemberg", "Lviv", "Redirecting to another page (Lemberg is another name of Lviv)")
        );
    }

    @BeforeEach
    public void openUrl() {
        getData().getDriver().navigate().to(TestConfig.getConfiguration().getString("baseUrlWiki"));
    }


    @Tag("regression")
    @MethodSource("dataProvider")
    @DisplayName("Perform search from the main page:")
    @ParameterizedTest(name = "{3}")
    void searchFromMainPageTest(String language, String request, String header, String description) {

        ArticlePage articlePage = new MainPage(getData())
                .selectSearchLanguage(language)
                .performSearch(request);

        Assertions.assertEquals(articlePage.getHeader(), header, "Check header");


    }
}
