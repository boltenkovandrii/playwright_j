package com.prestashop.test.runner;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.junit.Options;
import com.microsoft.playwright.junit.OptionsFactory;
import com.prestashop.test.base.TestConfig;

import java.nio.file.Path;
import java.nio.file.Paths;

@SuppressWarnings("checkstyle:MagicNumber")
public class PlaywrightOptions implements OptionsFactory {

    @Override
    @SuppressWarnings("checkstyle:Indentation")
    public Options getOptions() {

        String profile = TestConfig.getConfiguration().getString("profile", "Desktop Chrome");
        Path videoDir = Paths.get("build/playwright-output/videos");

        Options options = new Options()
                .setHeadless(TestConfig.getConfiguration().getBoolean("headless", false))
                .setOutputDir(Paths.get("build/playwright-output"))
                .setContextOptions(new Browser.NewContextOptions().setRecordVideoDir(videoDir));


        // supporting bunch of predefined profiles as well as a couple of custom ones.
        return switch (profile) {
            case "Desktop Chrome",
                 "Desktop Firefox",
                 "Desktop Safari",
                 "iPhone 13",
                 "iPhone 13 landscape",
                 "iPad Mini",
                 "iPad Mini landscape"
                    -> options.setDeviceName(profile);
            case "Desktop Chrome 2560x1600" ->
                    options
                            .setBrowserName("chromium")
                            .setContextOptions(new Browser.NewContextOptions()
                                    .setViewportSize(2560, 1600)
                                    .setDeviceScaleFactor(1.0)
                                    .setHasTouch(false)
                                    .setIsMobile(false)
                                    .setRecordVideoDir(videoDir)
                            );
            case "Desktop Firefox 2560x1600" ->
                    options
                            .setBrowserName("firefox")
                            .setContextOptions(new Browser.NewContextOptions()
                                    .setViewportSize(2560, 1600)
                                    .setDeviceScaleFactor(1.0)
                                    .setHasTouch(false)
                                    .setIsMobile(false)
                                    .setRecordVideoDir(videoDir)
                            );
            case "iPad Pro 11 Chromium" ->
                    options
                            .setBrowserName("chromium")
                            .setContextOptions(new Browser.NewContextOptions()
                                    .setViewportSize(834, 1194)
                                    .setDeviceScaleFactor(2.0)
                                    .setIsMobile(true)
                                    .setHasTouch(true)
                                    .setRecordVideoDir(videoDir)
                            );
            case "iPad Pro 11 landscape Chromium" ->
                    options
                            .setBrowserName("chromium")
                            .setContextOptions(new Browser.NewContextOptions()
                                    .setViewportSize(1194, 834)
                                    .setDeviceScaleFactor(2.0)
                                    .setIsMobile(true)
                                    .setHasTouch(true)
                                    .setRecordVideoDir(videoDir)
                            );


            default -> throw new IllegalArgumentException("Unsupported profile: " + profile);
        };

    }
}
