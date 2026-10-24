package com.andrii.test.tests;

import com.andrii.test.base.TestBase;
import com.andrii.test.base.TestConfig;
import com.andrii.test.pages.MainPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Featured languages suite")
class FeaturedLanguagesTest extends TestBase {

    @Test
    @Tag("regression")
    @DisplayName("Check list of featured languages")
    void featuredLanguagesTest() {

        getData().getDriver().navigate().to(TestConfig.getConfiguration().getString("baseUrlWiki"));
        MainPage mainPage = new MainPage(getData());
        List<String> featuredLanguages = mainPage.getFeaturedLanguages();

        assertThat(featuredLanguages).as("Check that list of featured languages equal to expected")
                .containsExactlyInAnyOrder("English",
                        "日本語",
                        "Deutsch",
                        "Русский",
                        "Español",
                        "Français",
                        "中文",
                        "Italiano",
                        "Polski",
                        "Português");

    }


}
