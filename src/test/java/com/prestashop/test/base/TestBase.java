package com.prestashop.test.base;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Tracing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.fail;

public class TestBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestBase.class);


    protected final void runScenario(Scenario scenario) {
        for (TestRunConfig run : PlaywrightSettings.testRuns()) {
            LOGGER.info("Running scenario for browser: {} and profile: {}", run.browser(), run.profile());

            runScenario(run, scenario);
        }
    }

    private void runScenario(TestRunConfig run, Scenario scenario) {
        Throwable finalFailure = null;
        int attempts = PlaywrightSettings.retries() + 1;
        for (int attempt = 1; attempt <= attempts; attempt++) {
            boolean retainArtifacts = attempt > 1;
            Path outputDirectory = artifactDirectory(run, attempt);
            try (Playwright playwright = Playwright.create()) {
                playwright.selectors().setTestIdAttribute("id");
                Browser browser = browser(playwright, run.browser());
                BrowserContext context = browser.newContext(run.profile().contextOptions().setRecordVideoDir(outputDirectory.resolve("video")));
                context.tracing().start(new Tracing.StartOptions().setScreenshots(true).setSnapshots(true).setSources(true));
                Page page = context.newPage();
                AllureArtifacts.setAttempt(retainArtifacts);

                try {
                    scenario.execute(page);
                    context.tracing().stop(new Tracing.StopOptions().setPath(outputDirectory.resolve("trace.zip")));
                    context.close();
                    browser.close();
                    if (retainArtifacts) {
                        attachArtifacts(outputDirectory);
                    } else {
                        deleteDirectory(outputDirectory);
                    }
                    return;
                } catch (Throwable failure) {
                    // Preserve the actual scenario failure.
                    finalFailure = failure;
                    // Diagnostics must never prevent another retry.
                    runBestEffort("capture failure screenshot",  () -> AllureArtifacts.failureScreenshot(page));

                    runBestEffort("save Playwright trace",
                            () -> context.tracing().stop(new Tracing.StopOptions().setPath(outputDirectory.resolve("trace.zip")))
                    );

                    runBestEffort("attach failure artifacts",  () -> attachArtifacts(outputDirectory));

                } finally {
                    AllureArtifacts.clearAttempt();

                    // Cleanup is independent: one failure must not skip the next action.
                    runBestEffort("close browser context", context::close);
                    runBestEffort("close browser", browser::close);
                }
            }
        }
        fail("Scenario failed for " + run + " after " + attempts + " attempt(s)", finalFailure);
    }

    private void runBestEffort(String operation, Runnable action) {
        try {
            action.run();
        } catch (Throwable diagnosticFailure) {
            System.err.printf(
                    "Failed to %s: %s%n",
                    operation,
                    diagnosticFailure.getMessage());
        }
    }

    private Browser browser(Playwright playwright, String browser) {
        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions().setHeadless(PlaywrightSettings.headless());
        return switch (browser) {
            case "chromium" -> playwright.chromium().launch(options);
            case "firefox" -> playwright.firefox().launch(options);
            case "webkit" -> playwright.webkit().launch(options);
            default -> throw new IllegalArgumentException("Unsupported Playwright browser: " + browser);
        };
    }

    private Path artifactDirectory(TestRunConfig run, int attempt) {
        try {
            Path parent = Files.createDirectories(Path.of("build", "playwright-artifacts",
                    run.browser(), run.profile().toString()));
            return Files.createTempDirectory(parent, "attempt-" + attempt + "-");

        } catch (IOException e) {
            throw new IllegalStateException("Unable to create Playwright artifact directory", e);
        }
    }

    private void attachArtifacts(Path outputDirectory) {
        Path trace = outputDirectory.resolve("trace.zip");
        if (Files.exists(trace)) {
            AllureArtifacts.attachFile(trace, "Trace", "application/zip");
        }
        Path videoDirectory = outputDirectory.resolve("video");
        if (Files.isDirectory(videoDirectory)) {
            try (Stream<Path> files = Files.list(videoDirectory)) {
                files.filter(path -> path.toString().endsWith(".webm"))
                        .forEach(path -> AllureArtifacts.attachFile(path, "Video", "video/webm"));
            } catch (IOException e) {
                throw new IllegalStateException("Unable to attach Playwright videos", e);
            }
        }
    }

    private void deleteDirectory(Path directory) {
        if (!Files.exists(directory)) {
            return;
        }
        try (Stream<Path> files = Files.walk(directory)) {
            files.sorted(Comparator.reverseOrder()).forEach(path -> {
                try {
                    Files.delete(path);
                } catch (IOException e) {
                    throw new IllegalStateException("Unable to remove temporary artifact " + path, e);
                }
            });
        } catch (IOException e) {
            throw new IllegalStateException("Unable to clean Playwright artifacts", e);
        }
    }
}
