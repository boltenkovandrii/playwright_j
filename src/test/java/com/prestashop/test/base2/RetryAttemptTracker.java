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

        String runId = System.getProperty(RUN_ID_PROPERTY);

        // Running directly from an IDE without Gradle retry tracking.
        if (runId == null || runId.isBlank()) {
            return 1;
        }

        String testId = context.getUniqueId() + "|" + run.browser() + "|" + run.profile();
        String testKey = sha256(testId);
        Path directory = Path.of("build","retry-state", runId, testKey);

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

    private static String sha256(String value) {
        try {
            byte[] hash = MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8));

            return HexFormat.of().formatHex(hash);

        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 is not available", e);
        }
    }
}