package com.prestashop.test.runner;

import com.prestashop.test.base.TestConfig;
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
    private static final int MAX_ATTEMPTS = TestConfig.getConfiguration().getInt("retryAttempts", 1) + 1;

    private RetryAttemptTracker() {
    }

    public static int nextAttempt(ExtensionContext context) {

        Path directory = stateDirectory(context); // Ensure the directory exists.

        if (directory == null) {
            return 1;
        }

        try {
            Files.createDirectories(directory);

            int attempt = 1;

            while (attempt <= MAX_ATTEMPTS) {
                Path marker = directory.resolve("attempt-" + attempt + ".started");
                try {
                    Files.createFile(marker);
                    return attempt;
                } catch (FileAlreadyExistsException ignored) {
                    attempt++;
                }
            }
            throw new IllegalStateException("Exceeded maximum retry attempts (" + MAX_ATTEMPTS + ") for test: " + context.getRequiredTestMethod().getName());
        } catch (IOException e) {
            throw new IllegalStateException("Unable to determine test retry attempt", e);
        }
    }

    private static Path stateDirectory(ExtensionContext context) {
        String runId = System.getProperty(RUN_ID_PROPERTY);
        if (runId == null || runId.isBlank()) {
            return null;
        }
        String testId = context.getRequiredTestClass().getName() + "|" + context.getRequiredTestMethod().toGenericString();
        String testKey = sha256(testId);
        return Path.of("build", "retry-state", runId, testKey);
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
