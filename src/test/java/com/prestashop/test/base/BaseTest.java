package com.prestashop.test.base;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Tracing;
import com.microsoft.playwright.junit.UsePlaywright;
import com.prestashop.test.runner.ArtifactWatcher;
import com.prestashop.test.runner.PlaywrightOptions;
import com.prestashop.test.runner.RetryAttemptTracker;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.nio.file.Files;
import java.nio.file.Path;


@UsePlaywright(PlaywrightOptions.class)
@ExtendWith(ArtifactWatcher.class)
public class BaseTest {

    @BeforeEach
    void setUp(Page page, Playwright playwright, BrowserContext context, Browser browser, ExtensionContext extensionContext) throws Exception {
        // Setting the testId attribute for Playwright selectors to "id"
        playwright.selectors().setTestIdAttribute("id");

        // Tracking the retry attempt for the current test and storing it in the ArtifactWatcher
        int attempt = RetryAttemptTracker.nextAttempt(extensionContext);
        ArtifactWatcher.setAttempt(extensionContext, attempt);
        ArtifactWatcher.setVideoPath(extensionContext, page.video() != null ? page.video().path() : null);

        // Adding the profile as a label to Allure report
        String profile = TestConfig.getConfiguration().getString("profile");
        Allure.label("profile", profile);

        //Setting up the path for trace artifacts and starting the tracing for the test
        Path tracePath = Path.of(
                "build",
                "test-artifacts",
                profile.replaceAll("[^a-zA-Z0-9._-]", "_"),
                extensionContext.getRequiredTestClass().getName(),
                extensionContext.getRequiredTestMethod().getName(),
                "attempt-" + attempt,
                "trace.zip"
        );
        Files.createDirectories(tracePath.getParent());
        ArtifactWatcher.setTracePath(extensionContext, tracePath);
        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true));


    }


    @AfterEach
    void tearDown(Page page, Playwright playwright, BrowserContext context, Browser browser, ExtensionContext extensionContext) {
        context.tracing().stop(new Tracing.StopOptions().setPath(ArtifactWatcher.getTracePath(extensionContext)));
        page.close();
        AllureArtifacts.clearAttempt();
    }
}
