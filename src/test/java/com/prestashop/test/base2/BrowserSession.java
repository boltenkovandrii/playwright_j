package com.prestashop.test.base2;

import com.microsoft.playwright.*;
import com.prestashop.test.base.PlaywrightSettings;
import com.prestashop.test.base.TestRunConfig;
import io.qameta.allure.Allure;
import io.qameta.allure.AttachmentOptions;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

public final class BrowserSession implements AutoCloseable {

    private final Playwright playwright;
    private final Browser browser;
    private final BrowserContext context;
    private final Page page;
    private final Path outputDirectory;

    private boolean closed;

    private BrowserSession(Playwright playwright, Browser browser, BrowserContext context, Page page, Path outputDirectory) {
        this.playwright = playwright;
        this.browser = browser;
        this.context = context;
        this.page = page;
        this.outputDirectory = outputDirectory;
    }

    public static BrowserSession start(TestRunConfig run, int attempt) {
        Path outputDirectory = createOutputDirectory(run, attempt);

        Playwright playwright = null;
        Browser browser = null;
        BrowserContext context = null;

        try {
            playwright = Playwright.create();
            playwright.selectors().setTestIdAttribute("id");

            browser = launchBrowser(playwright, run.browser());

            context = browser.newContext(run.profile().contextOptions().setRecordVideoDir(outputDirectory.resolve("video")));
            context.tracing().start(new Tracing.StartOptions().setScreenshots(true).setSnapshots(true).setSources(true));

            Page page = context.newPage();

            return new BrowserSession(playwright, browser, context, page, outputDirectory);

        } catch (Throwable failure) {
            closeQuietly(context);
            closeQuietly(browser);
            closeQuietly(playwright);
            throw failure;
        }
    }

    public Page page() {
        return page;
    }

    public void finishPassed() {
        stopTrace(null);
        close();
        deleteDirectory(outputDirectory);
    }

    public void finishFailed() {
        captureFailureScreenshot();
        stopTrace(outputDirectory.resolve("trace.zip"));

        // Closing the context finalizes its video recording.
        close();

        attachVideoIfPresent();
        attachTraceIfPresent();
    }

    private void captureFailureScreenshot() {
        try {
            byte[] screenshot = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
            Allure.attachment("Failure state", "image/png", new ByteArrayInputStream(screenshot), AttachmentOptions.withFileExtension(".png"));
        } catch (Throwable failure) {
            logDiagnosticFailure("capture screenshot", failure);
        }
    }

    private void stopTrace(Path destination) {
        try {
            Tracing.StopOptions options = new Tracing.StopOptions();

            if (destination != null) {
                options.setPath(destination);
            }

            context.tracing().stop(options);

        } catch (Throwable failure) {
            logDiagnosticFailure("stop tracing", failure);
        }
    }

    private void attachVideoIfPresent() {
        try (var files = Files.list(outputDirectory.resolve("video"))) {
            for (Path video : files.filter(Files::isRegularFile).toList()) {
                attachFile(video, "Video - " + video.getFileName(), "video/webm");
            }
        } catch (Throwable failure) {
            logDiagnosticFailure("attach video", failure);
        }
    }

    private void attachTraceIfPresent() {
        Path trace = outputDirectory.resolve("trace.zip");

        if (Files.isRegularFile(trace)) {
            attachFile(trace, "Playwright trace", "application/zip");
        }
    }

    private static void attachFile(Path path, String name, String contentType) {
        try (var input = Files.newInputStream(path)) {
            Allure.attachment(name, contentType, input, AttachmentOptions.withFileExtension(extension(path)));
        } catch (Throwable failure) {
            logDiagnosticFailure("attach " + path, failure);
        }
    }

    @Override
    public void close() {
        if (closed) {
            return;
        }

        closed = true;

        closeQuietly(context);
        closeQuietly(browser);
        closeQuietly(playwright);
    }

    private static Browser launchBrowser(Playwright playwright, String name) {
        BrowserType.LaunchOptions options =
                new BrowserType.LaunchOptions()
                        .setHeadless(PlaywrightSettings.headless());

        return switch (name) {
            case "chromium" -> playwright.chromium().launch(options);
            case "firefox" -> playwright.firefox().launch(options);
            case "webkit" -> playwright.webkit().launch(options);
            default -> throw new IllegalArgumentException(
                    "Unsupported Playwright browser: " + name);
        };
    }

    private static Path createOutputDirectory(TestRunConfig run, int attempt) {
        try {
            Path parent = Files.createDirectories(
                    Path.of("build", "playwright-artifacts",
                            run.browser(), run.profile().toString()));

            return Files.createTempDirectory(
                    parent,
                    "attempt-" + attempt + "-" + UUID.randomUUID() + "-");

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Unable to create artifact directory", e);
        }
    }

    private static void deleteDirectory(Path directory) {
        if (directory == null || !Files.exists(directory)) {
            return;
        }

        try (var paths = Files.walk(directory)) {
            paths.sorted((a, b) -> b.compareTo(a))
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException e) {
                            logDiagnosticFailure("delete " + path, e);
                        }
                    });
        } catch (IOException e) {
            logDiagnosticFailure("delete artifact directory", e);
        }
    }

    private static String extension(Path path) {
        String name = path.getFileName().toString();
        int index = name.lastIndexOf('.');
        return index < 0 ? "" : name.substring(index);
    }

    private static void closeQuietly(AutoCloseable resource) {
        if (resource == null) {
            return;
        }

        try {
            resource.close();
        } catch (Throwable failure) {
            logDiagnosticFailure("close resource", failure);
        }
    }

    private static void logDiagnosticFailure(String operation, Throwable failure) {
        System.err.printf("Unable to %s: %s%n", operation, failure);
    }
}