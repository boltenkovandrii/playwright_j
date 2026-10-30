package com.prestashop.test.base;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class PlaywrightSettings {

    private PlaywrightSettings() {
    }

    public static List<TestRunConfig> testRuns() {
        List<String> browsers = values("browsers", "chromium");
        List<BrowserProfile> profiles = values("profiles", "desktop_1920x1200")
                .stream()
                .map(BrowserProfile::fromName)
                .toList();

        List<TestRunConfig> runs = browsers.stream()
                .flatMap(browser -> profiles.stream()
                        .filter(profile -> supports(browser, profile))
                        .map(profile -> new TestRunConfig(browser, profile)))
                .toList();
        if (runs.isEmpty()) {
            throw new IllegalArgumentException("No supported browser/profile combinations: " + browsers + " / " + profiles);
        }
        return runs;
    }

    public static int retries() {
        return Integer.parseInt(System.getProperty("pwRetries",
                System.getProperty("retries", "1")));
    }

    public static boolean screenshotsEnabled() {
        return Boolean.parseBoolean(System.getProperty("allureScreenshots", "false"));
    }

    public static boolean headless() {
        return Boolean.parseBoolean(System.getProperty("headless", "true"));
    }

    public static String baseUrl() {
        return System.getProperty("baseUrlPrestaShop", "http://localhost:8090").replaceAll("/+$", "");
    }

    private static List<String> values(String systemProperty, String defaultValue) {
        String value = System.getProperty(systemProperty, defaultValue);
        return Arrays.stream(value.split(","))
                .map(String::trim)
                .filter(item -> !item.isEmpty())
                .collect(Collectors.toList());
    }

    private static boolean supports(String browser, BrowserProfile profile) {
        if (!List.of("chromium", "firefox", "webkit").contains(browser)) {
            throw new IllegalArgumentException("Unsupported Playwright browser: " + browser);
        }
        return !"firefox".equals(browser) || profile.isDesktop();
    }
}
