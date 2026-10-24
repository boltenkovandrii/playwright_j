package com.andrii.test.tests;

import com.andrii.test.base.TestBase;
import com.andrii.test.base.TestConfig;
import com.andrii.test.pages.MainPage;
import com.andrii.test.pages.commons.CommonsFilePage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.andrii.test.base.FileUtils.fileExists;
import static com.andrii.test.base.FileUtils.getDownloadsPath;
import static com.andrii.test.base.FileUtils.removeFile;
import static com.andrii.test.base.FileUtils.waitForFile;


@DisplayName("WikiMedia files suite")
class DownloadFileTest extends TestBase {

    private final String fileName = "ECHO_Ukraine_Editable_A4_Landscape.pdf";

    @Test
    @Tag("current")
    @Tag("regression")
    @DisplayName("Download pdf file from WikiMedia")
    void downloadPdfFileTest() {

        getData().getDriver().navigate().to(TestConfig.getConfiguration().getString("baseUrlWiki"));

        CommonsFilePage commonsFilePage = new MainPage(getData())
                .dismissBanner()
                .clickCommons()
                .selectLanguage("English")
                .performSearch(fileName)
                .openOtherMedia()
                .selectFileType("pdf")
                .openFirstResult();

        String filePath = getDownloadsPath() + fileName;
        removeFile(filePath);
        commonsFilePage
                .clickDownload()
                .downloadFullSize();

        waitForFile(filePath);
        Assertions.assertTrue(fileExists(filePath), "Check if file is downloaded");
    }
}
