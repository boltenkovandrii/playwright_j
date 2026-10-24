package com.andrii.test.pages;

import com.andrii.test.base.TestConfigurationData;
import com.andrii.test.pages.commons.CommonsMainPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

public class MainPage extends PageBase {

    @FindBy(css = "div[class*='central-featured-lang'] a strong")
    private List<WebElement> featuredLanguages;

    @FindBy(css = "select[id='searchLanguage']")
    private WebElement searchLanguageSelect;

    @FindBy(css = "input[id='searchInput']")
    private WebElement searchInput;

    @FindBy(css = "button[class*='pure-button']")
    private WebElement performSearchButton;

    @FindBy(css = "div.svg-Commons-logo_sister")
    private WebElement commonsLogoIcon;

    @FindBy(css = "button.banner__close")
    private WebElement bannerCloseButton;

    public MainPage(TestConfigurationData data) {
        super(data);
    }

    @Override
    public void waitForLoadingHook() {
        waitTillElementClickable(searchInput);
    }

    public List<String> getFeaturedLanguages() {
        return featuredLanguages.stream().map(WebElement::getText).collect(Collectors.toList());
    }

    public MainPage selectSearchLanguage(String language) {
        searchLanguageSelect.click();
        searchLanguageSelect.findElement(By.xpath(".//option[text()='" + language + "']")).click();
        return this;
    }

    public ArticlePage performSearch(String searchPhrase) {
        waitAndSendKeys(searchInput, searchPhrase);
        waitAndClick(performSearchButton);
        return new ArticlePage(data);
    }

    public MainPage dismissBanner() {
        if (isElementPresent(bannerCloseButton) && bannerCloseButton.isDisplayed()) {
            bannerCloseButton.click();
        }
        return this;
    }

    public CommonsMainPage clickCommons() {
        waitAndClick(commonsLogoIcon);
        return new CommonsMainPage(data);
    }
}
