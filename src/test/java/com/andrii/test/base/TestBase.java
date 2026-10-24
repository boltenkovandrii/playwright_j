package com.andrii.test.base;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestBase.class);
    private static final Duration PAGE_LOAD_TIMEOUT = Duration.ofSeconds(90);

    private TestConfigurationData data = null;

    public TestConfigurationData getData() {
        return this.data;
    }

    @AfterEach
    public void afterEach() {
        try {
            data.getEventListener().makeScreenshot(data.getDriver(), "at the end of the test");
            data.getEventListener().makePageDump(data.getDriver(), data.getBrowser());
        } catch (final Exception e) {
            LOGGER.error("Error on logging. Presumably not a problem: {}", e.getMessage());
        }
    }

    @AfterAll
    public void afterAll() {
        data.getDriver().quit();
    }



    @BeforeAll
    public void beforeAll() {

        data = new TestConfigurationData();
        data.setEventListener(new EventListener());

        try {

            WebDriver webDriver = WebDriverManager.init(data);
            data.getEventListener().setDriver(webDriver);
            WebDriver decoratedWebDriver = new EventFiringDecorator<>(data.getEventListener()).decorate(webDriver);
            data.setDriver(decoratedWebDriver);
            data.getDriver().manage().window().maximize();
            data.getDriver().manage().timeouts().pageLoadTimeout(PAGE_LOAD_TIMEOUT);


        } catch (Exception e) {
            LOGGER.error("Error on setting configuration data", e);
        }
    }


}
