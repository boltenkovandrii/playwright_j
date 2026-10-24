package com.andrii.test.pages.commons;

import com.andrii.test.base.TestConfigurationData;
import com.andrii.test.pages.PageBase;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CommonsFilePage extends PageBase {

    @FindBy(css = "a[title='Download all sizes']")
    private WebElement downloadButton;

    @FindBy(linkText = "Full resolution")
    private WebElement fullSizeDownloadLink;


    public CommonsFilePage(TestConfigurationData data) {
        super(data);
    }

    @Override
    public void waitForLoadingHook() {
        //implement if needed
    }


    public CommonsFilePage clickDownload() {
        waitAndClick(downloadButton);
        return new CommonsFilePage(data);
    }

    public CommonsFilePage downloadFullSize() {
        waitAndClick(fullSizeDownloadLink);
        return new CommonsFilePage(data);
    }


}
