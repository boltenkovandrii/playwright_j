package com.andrii.test.base;

import io.qameta.allure.Allure;
import io.qameta.allure.AttachmentOptions;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.support.events.WebDriverListener;

import java.io.ByteArrayInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class EventListener implements WebDriverListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventListener.class);
    private WebDriver driver;

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }


    @Override
    public void beforeSendKeys(WebElement element, CharSequence... keysToSend) {
        highlightElement(driver, element);
    }

    @Override
    public void afterSendKeys(WebElement element, CharSequence... keysToSend) {
        try {
            makeScreenshot(driver, "After changing value of element " + element.getText());
        } catch (final StaleElementReferenceException e) {
            makeScreenshot(driver, "After changing value of element. Element updated.");
        }
    }

    @Override
    public void beforeClick(final WebElement element) {
        highlightElement(driver, element);
        makeScreenshot(driver, "Before clicking on element " + element.getText());
    }

    @Override
    public void onError(Object target, Method method, Object[] args, InvocationTargetException e) {
        Throwable error = e.getCause();
        if (error.getClass().equals(NoSuchElementException.class)) {
            //In most cases NoSuchElementException occurs as result of calling WebDriverWait.until() and does not need to be logged.
            // Actual error will break the test and final screenshot will be enough for the investigation.
            return;
        } else if (error.getClass().equals(NoSuchSessionException.class)) {
            LOGGER.error("WebDriver error: session not found - sometimes occur on firefox driver closing - not an error in this case, " +
                    "but it is too late to make screenshot");
        } else {
            try {
                makeScreenshot(driver, "WebDriver error: ", error);
            } catch (WebDriverException we) {
                LOGGER.error("Error on making screenshot. Can occur in rare cases for example if driver is closed already. Presumably not a problem: {}",
                        e.getMessage());
            }
        }
    }


    private void highlightElement(final WebDriver driver, final WebElement element) {
        final JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, element.getAttribute("style") + " color: red; border: 2px solid red;");
    }


    public void makeScreenshot(WebDriver driver, String screenshotDescription, Throwable error) {
        makeScreenshot(driver, screenshotDescription + "See error below");
        LOGGER.error("", error);
    }


    public void makeScreenshot(WebDriver driver, String screenshotDescription) {
        TakesScreenshot ts = (TakesScreenshot) driver;
        Allure.attachment("Screenshot " + screenshotDescription,
                "image/png",
                new ByteArrayInputStream(ts.getScreenshotAs(OutputType.BYTES)),
                AttachmentOptions.withFileExtension(".png"));
        LOGGER.info("Screenshot made: {} ", screenshotDescription);
    }

    public void makePageDump(WebDriver driver, String browserName) {

        Allure.attachment("Page source: ", "text/html", driver.getPageSource(), AttachmentOptions.withFileExtension(".html"));

        if ("chrome".equals(browserName) || "edge".equals(browserName)) { //browser logs catching is not supported for firefox for now
            StringBuilder logs = new StringBuilder();
            for (LogEntry le : driver.manage().logs().get(LogType.BROWSER).getAll()) {
                logs.append("\n").append(le.getMessage());
            }
            Allure.attachment("Browser console logs", logs.toString());
        }

    }

}
