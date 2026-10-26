package com.prestashop.test.base;

import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;
import io.qameta.allure.AttachmentOptions;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class AllureArtifacts {

    private static final ThreadLocal<Attempt> ATTEMPT = new ThreadLocal<>();

    private AllureArtifacts() {
    }

    static void setAttempt(boolean retainArtifacts) {
        ATTEMPT.set(new Attempt(retainArtifacts));
    }

    static void clearAttempt() {
        ATTEMPT.remove();
    }

    public static void screenshot(Page page, String name) {
        Attempt attempt = ATTEMPT.get();
        if (attempt != null && (attempt.retainArtifacts || PlaywrightSettings.screenshotsEnabled())) {
            attachPng(page.screenshot(new Page.ScreenshotOptions().setFullPage(true)), name);
        }
    }

    static void failureScreenshot(Page page) {
        attachPng(page.screenshot(new Page.ScreenshotOptions().setFullPage(true)), "Failure state");
    }

    static void attachFile(Path path, String name, String type) {
        try {
            Allure.attachment(name, type, Files.newInputStream(path), AttachmentOptions.withFileExtension(fileExtension(path)));

        } catch (IOException e) {
            throw new IllegalStateException("Unable to attach " + path, e);
        }
    }

    private static void attachPng(byte[] bytes, String name) {
        Allure.attachment(name, "image/png", new ByteArrayInputStream(bytes), AttachmentOptions.withFileExtension(".png"));
    }

    private static String fileExtension(Path path) {
        String name = path.getFileName().toString();
        int index = name.lastIndexOf('.');
        return index >= 0 ? name.substring(index) : "";
    }

    private record Attempt(boolean retainArtifacts) {
    }
}
