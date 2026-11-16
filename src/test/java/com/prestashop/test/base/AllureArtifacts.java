package com.prestashop.test.base;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Locator;
import io.qameta.allure.Allure;
import io.qameta.allure.AttachmentOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class AllureArtifacts {

    // Store attempt state per thread to prevent interference during parallel test execution.
    // Each invocation sets its own state and removes it after execution,
    // preventing state from leaking when worker threads are reused.
    private static final ThreadLocal<Attempt> ATTEMPT = new ThreadLocal<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(AllureArtifacts.class);
    private static final boolean SCREENSHOTS_ENABLED = Boolean.parseBoolean(TestConfig.getConfiguration().getString("allureScreenshots", "false"));

    private AllureArtifacts() {
    }

    public static void setAttempt(boolean retainArtifacts) {
        ATTEMPT.set(new Attempt(retainArtifacts));
    }

    public static void clearAttempt() {
        ATTEMPT.remove();
    }

    public static void screenshot(Page page, String name) {
        Attempt attempt = ATTEMPT.get();
        if (attempt != null && (attempt.retainArtifacts || SCREENSHOTS_ENABLED)) {
            attachPng(page.screenshot(new Page.ScreenshotOptions().setFullPage(true)), name);
        } else {
            Allure.step(name);
        }
    }

    public static void screenshot(Locator locator, String name) {
        Attempt attempt = ATTEMPT.get();
        if (attempt != null && (attempt.retainArtifacts || SCREENSHOTS_ENABLED)) {
            attachPng(locator.screenshot(), name);
        } else {
            Allure.step(name);
        }
    }

    public static void attachFile(Path path, String name, String contentType) {
        if (path == null || !Files.isRegularFile(path)) {
            LOGGER.warn("Artifact file does not exist: {}", path);
            return;
        }

        try (var input = Files.newInputStream(path)) {
            Allure.attachment(name, contentType, input, AttachmentOptions.withFileExtension(extension(path)));
        } catch (IOException failure) {
            Allure.step("Failed to attach artifact: " + name + " - " + failure.getMessage());
            logDiagnosticFailure("attach " + path, failure);
        }
    }

    private static void attachPng(byte[] bytes, String name) {
        Allure.attachment(name, "image/png", new ByteArrayInputStream(bytes), AttachmentOptions.withFileExtension(".png"));
    }

    private record Attempt(boolean retainArtifacts) {
    }

    private static void logDiagnosticFailure(String operation, Throwable failure) {
        LOGGER.error("Unable to {}: {}", operation, failure.toString(), failure);
    }

    private static String extension(Path path) {
        String name = path.getFileName().toString();
        int index = name.lastIndexOf('.');
        return index < 0 ? "" : name.substring(index);
    }

}
