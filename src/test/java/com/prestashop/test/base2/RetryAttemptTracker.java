package com.prestashop.test.base2;

import com.prestashop.test.base.TestRunConfig;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public final class RetryAttemptTracker {

    private static final String RUN_ID_PROPERTY = "retryTrackerRunId";

    private RetryAttemptTracker() {
    }

    public static int nextAttempt(ExtensionContext context, TestRunConfig run) {

        Path directory = stateDirectory(context, run); // Ensure the directory exists.

        try {
            Files.createDirectories(directory);

            for (int attempt = 1; ; attempt++) {
                Path marker = directory.resolve("attempt-" + attempt + ".started");

                try {
                    Files.createFile(marker);
                    return attempt;

                } catch (FileAlreadyExistsException ignored) {
                    // This attempt number is already taken.
                    // Try the next one.
                }
            }

        } catch (IOException e) {
            throw new IllegalStateException("Unable to determine test retry attempt", e);
        }
    }

    private static Path stateDirectory(ExtensionContext context, TestRunConfig run) {
        String runId = System.getProperty(RUN_ID_PROPERTY);
        if (runId == null || runId.isBlank()) {
            return null;
        }
        String testId = context.getRequiredTestClass().getName() + "|" + context.getRequiredTestMethod().toGenericString() + "|" + run.browser() + "|" + run.profile();
        String testKey = sha256(testId);
        return Path.of("build","retry-state", runId, testKey);
    }

    public static boolean hasPassed(ExtensionContext context, TestRunConfig run) {
        Path directory = stateDirectory(context, run);
        return directory != null && Files.isRegularFile(directory.resolve("passed"));
    }

    public static void markPassed(ExtensionContext context, TestRunConfig run) {
        Path directory = stateDirectory(context, run);
        if (directory == null) {
            return;
        }
        try {
            Files.createDirectories(directory);
            Path marker = directory.resolve("passed");
            try {
                Files.createFile(marker);
            } catch (FileAlreadyExistsException ignored) {
                // Already marked as passed.
            }

        } catch (IOException e) {
            throw new IllegalStateException("Unable to save test pass status", e);
        }
    }


    private static String sha256(String value) {
        try {
            byte[] hash = MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 is not available", e);
        }
    }
}