package com.andrii.test.pages;

import com.andrii.test.base.TestConfigurationData;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;


public abstract class PageBase {

    protected final TestConfigurationData data;
    private final Duration waitDuration = Duration.ofSeconds(10);
    private final Duration pageLoadWaitDuration = Duration.ofSeconds(30);

    public PageBase(TestConfigurationData data) {
        this.data = data;
        PageFactory.initElements(data.getDriver(), this);
        waitForPageReady();
        waitForLoadingHook();
    }

    public void scrollToElement(WebElement element) {
        ((JavascriptExecutor) data.getDriver()).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public boolean isElementPresent(final WebElement element) {
        try {
            element.getTagName(); //just attempting to access element
            return true;
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    public void waitTillElementDisappear(final WebElement element) {
        waitTillElementDisappear(element, new WebDriverWait(data.getDriver(), waitDuration));
    }

    public void waitTillElementDisappear(final WebElement element, WebDriverWait wait) {
        wait.until((ExpectedCondition<Boolean>) driver -> {
            try {
                element.getTagName(); //just attempting to access element
                return false;
            } catch (NoSuchElementException | StaleElementReferenceException e) {
                return true;
            }
        });
    }

    public void waitTillElementClickable(WebElement element) {
        waitTillElementClickable(element, Duration.ofSeconds(2));
    }

    public void waitTillElementClickable(WebElement element, Duration duration) {
        WebDriverWait webDriverWait = new WebDriverWait(data.getDriver(), duration);
        webDriverWait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public abstract void waitForLoadingHook();

    protected final void waitForPageReady() {
        new WebDriverWait(data.getDriver(), pageLoadWaitDuration).until(
                webDriver -> "complete".equals(((JavascriptExecutor) webDriver).executeScript("return document.readyState"))
        );
    }

    public void waitAndClick(final WebElement element) {
        try {
            waitTillElementClickable(element);
            element.click();
        } catch (WebDriverException e) {
            data.getEventListener().makeScreenshot(data.getDriver(), "Cannot click on element:" + element, e);
            throw e;
        }
    }

    public void waitAndSendKeys(WebElement el, String text) {
        try {
            if (text != null) {
                waitTillElementClickable(el);
                el.clear();
                el.sendKeys(text);
            }
        } catch (StaleElementReferenceException se) {
            data.getEventListener().makeScreenshot(data.getDriver(), "StaleElementReferenceException for " + el);
            throw se;
        } catch (TimeoutException te) {
            data.getEventListener().makeScreenshot(data.getDriver(), "TimeoutException for " + el);
            throw te;
        }
    }

}
