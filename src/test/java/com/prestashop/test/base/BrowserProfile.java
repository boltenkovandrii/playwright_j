package com.prestashop.test.base;

import com.microsoft.playwright.Browser;

import java.util.Arrays;

public enum BrowserProfile {
    DESKTOP_2560X1600("desktop_2560x1600", 2560, 1600, false),
    DESKTOP_1920X1200("desktop_1920x1200", 1920, 1200, false),
    MOBILE_LANDSCAPE("mobile_landscape", 844, 390, true),
    MOBILE("mobile", 390, 844, true),
    TABLET_LANDSCAPE("tablet_landscape", 1024, 768, true),
    TABLET("tablet", 768, 1024, true);

    private final String name;
    private final int width;
    private final int height;
    private final boolean deviceEmulation;

    BrowserProfile(String name, int width, int height, boolean deviceEmulation) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.deviceEmulation = deviceEmulation;
    }

    public static BrowserProfile fromName(String name) {
        return Arrays.stream(values())
                .filter(profile -> profile.name.equals(name.trim()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported profile: " + name));
    }

    public Browser.NewContextOptions contextOptions() {
        Browser.NewContextOptions options = new Browser.NewContextOptions()
                .setViewportSize(width, height);
        if (deviceEmulation) {
            options.setIsMobile(true).setHasTouch(true);
        }
        return options;
    }

    public boolean isDesktop() {
        return !deviceEmulation;
    }

    @Override
    public String toString() {
        return name;
    }
}
