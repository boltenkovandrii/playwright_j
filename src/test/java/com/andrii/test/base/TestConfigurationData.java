package com.andrii.test.base;

import org.apache.commons.configuration2.CompositeConfiguration;
import org.openqa.selenium.WebDriver;

public class TestConfigurationData {

    private final String browser;
    private final boolean useGrid;
    private WebDriver driver;
    private EventListener eventListener;

    public TestConfigurationData() {
        CompositeConfiguration config = TestConfig.getConfiguration();
        browser = config.getString("browser", "chrome");
        useGrid = config.getBoolean("useGrid", false);
    }

    public boolean isUseGrid() {
        return useGrid;
    }

    public String getBrowser() {
        return browser;
    }

    public WebDriver getDriver() {
        return driver;
    }

    public EventListener getEventListener() {
        return eventListener;
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }
}
