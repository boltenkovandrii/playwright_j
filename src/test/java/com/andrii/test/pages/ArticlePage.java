package com.andrii.test.pages;

import com.andrii.test.base.TestConfigurationData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

public class ArticlePage extends PageBase {

    @FindBy(css = "h1[id='firstHeading']")
    private WebElement header;

    @FindBy(css = "div[class='mwe-popups-container']")
    private WebElement popup;

    @FindBy(css = "div[class='mwe-popups-container']>a>img[class='mwe-popups-thumbnail']")
    private WebElement popupImage;

    @FindBy(css = "div[class='mwe-popups-container']>a[class='mwe-popups-extract']>p")
    private WebElement popupExtract;


    public ArticlePage(TestConfigurationData data) {
        super(data);
    }

    @Override
    public void waitForLoadingHook() {
        waitTillElementClickable(header);
    }

    public String getHeader() {
        return header.getText();
    }

    public ArticlePage hoverOverSuitableLink(String linkName) {
        WebElement link = data.getDriver().findElement(By.xpath("//a[text()='" + linkName + "']")); //selecting first suitable element
        scrollToElement(link);
        new Actions(data.getDriver()).moveToElement(link).perform();
        waitTillElementClickable(popup); //waiting 0.5 sec for timeout
        return this;
    }

    public boolean checkPopupIsVisible(boolean checkText, boolean checkThumbnail) {
        return (!checkText || popupExtract.isDisplayed()) && (!checkThumbnail || popupImage.isDisplayed());
    }

}
