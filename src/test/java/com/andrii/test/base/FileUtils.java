package com.andrii.test.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;

import static java.io.File.separator;

public class FileUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);
    private static final int WAIT_FOR_FILE_SECONDS = 60;
    private static final long POLL_FOR_FILE_MILLISECONDS = 1000L;

    public static String getDownloadsPath() {
        if (TestConfig.getConfiguration().getBoolean("useGrid", false)) {
            return TestConfig.getConfiguration().getString("downloads") + separator;
        } else {
            return System.getProperty("user.home") + separator + "Downloads" + separator;
        }
    }

    public static void removeFile(String path) {
        LOGGER.info("Removing file: {}", path);
        try {
            Files.deleteIfExists(Paths.get(path));
        } catch (IOException e) {
            LOGGER.error("Failed to remove file: ", e);
        }
    }

    public static boolean fileExists(String path) {
        LOGGER.info("Checking if file exists: {}", path);
        return Files.exists(Paths.get(path), LinkOption.NOFOLLOW_LINKS);
    }

    public static void waitForFile(String path) {
        waitForFile(path, WAIT_FOR_FILE_SECONDS, 1);
    }


    public static void waitForFile(String path, int durationSeconds, int pollSeconds) {
        LOGGER.info("Waiting for file to be present: {}", path);
        int waited = 0;
        while (waited < durationSeconds) {
            if (fileExists(path)) {
                LOGGER.info("File present: {}", path);
                return;
            }
            try {
                Thread.sleep(pollSeconds * POLL_FOR_FILE_MILLISECONDS);
                waited = waited + pollSeconds;
            } catch (InterruptedException e) {
                LOGGER.error("Exception on waiting for file: ", e);
            }
        }

        LOGGER.error("File absent: {}", path);
    }

}
