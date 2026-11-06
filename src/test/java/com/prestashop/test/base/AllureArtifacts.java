package com.prestashop.test.base;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Locator;
import io.qameta.allure.Allure;
import io.qameta.allure.AttachmentOptions;

import java.io.ByteArrayInputStream;

public final class AllureArtifacts {

    private static final ThreadLocal<Attempt> ATTEMPT = new ThreadLocal<>();

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
        if (attempt != null && (attempt.retainArtifacts || PlaywrightSettings.screenshotsEnabled())) {
            attachPng(page.screenshot(new Page.ScreenshotOptions().setFullPage(true)), name);
        }
        else {
            Allure.step(name);
        }
    }

    public static void screenshot(Locator locator, String name) {
        Attempt attempt = ATTEMPT.get();
        if (attempt != null && (attempt.retainArtifacts || PlaywrightSettings.screenshotsEnabled())) {
            attachPng(locator.screenshot(), name);
        }
        else{
            Allure.step(name);
        }
    }

    private static void attachPng(byte[] bytes, String name) {
        Allure.attachment(name, "image/png", new ByteArrayInputStream(bytes), AttachmentOptions.withFileExtension(".png"));
    }

    private record Attempt(boolean retainArtifacts) {}
}
