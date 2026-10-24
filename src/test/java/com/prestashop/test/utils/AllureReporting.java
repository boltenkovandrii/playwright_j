package com.prestashop.test.utils;

import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;
import io.qameta.allure.AttachmentOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.ByteArrayInputStream;

public class AllureReporting {
    private static final Logger LOGGER = LoggerFactory.getLogger(AllureReporting.class);

    public void makeScreenshot(Page page, String screenshotDescription) {

        Allure.attachment("Screenshot " + screenshotDescription,
                "image/png",
                new ByteArrayInputStream(page.screenshot()),
                AttachmentOptions.withFileExtension(".png"));
        LOGGER.info("Screenshot made: {} ", screenshotDescription);
    }

}
