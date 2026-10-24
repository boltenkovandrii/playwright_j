package com.andrii.test.pages.commons;

import com.andrii.test.base.TestConfigurationData;
import com.andrii.test.pages.PageBase;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

public class CommonsMainPage extends PageBase {

    @FindBy(id = "vector-main-menu-dropdown")
    private WebElement mainMenu;

    @FindBy(id = "mw-AnonymousI18N-picker")
    private WebElement languageSelect;

    @FindBy(id = "searchInput")
    private WebElement searchInput;

    @FindBy(css = "div.vector-header-end button")
    private WebElement searchButton;


    public CommonsMainPage(TestConfigurationData data) {
        super(data);
    }

    @Override
    public void waitForLoadingHook() {
        waitTillElementClickable(mainMenu);
    }


    public CommonsMainPage selectLanguage(String language) {
        waitAndClick(mainMenu);
        Select searchLanguageSelect = new Select(this.languageSelect);
        searchLanguageSelect.selectByVisibleText(language);
        return new CommonsMainPage(data);
    }

    public CommonsSearchResultPage performSearch(String searchPhrase) {
        waitAndSendKeys(searchInput, searchPhrase);
        waitAndClick(searchButton);
        return new CommonsSearchResultPage(data);
    }

}
