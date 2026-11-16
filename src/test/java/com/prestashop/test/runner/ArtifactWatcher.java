package com.prestashop.test.runner;


import com.prestashop.test.base.AllureArtifacts;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.TestWatcher;

import java.nio.file.Path;

public final class ArtifactWatcher implements TestWatcher, ParameterResolver {

    private static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(ArtifactWatcher.class);

    private static final String ATTEMPT = "attempt";
    private static final String VIDEO_PATH = "videoPath";
    private static final String TRACE_PATH = "tracePath";

    public static void setAttempt(ExtensionContext context, int attempt) {
        context.getStore(NAMESPACE).put(ATTEMPT, attempt);
        AllureArtifacts.setAttempt(attempt > 1);
    }

    public static void setVideoPath(ExtensionContext context, Path path) {
        context.getStore(NAMESPACE).put(VIDEO_PATH, path);
    }

    public static void setTracePath(ExtensionContext context, Path path) {
        context.getStore(NAMESPACE).put(TRACE_PATH, path);
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        attachArtifacts(context);
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        Integer attempt = context.getStore(NAMESPACE).get(ATTEMPT, Integer.class);

        if (attempt != null && attempt > 1) {
            attachArtifacts(context);
        }
    }

    private void attachArtifacts(ExtensionContext context) {
        ExtensionContext.Store store = context.getStore(NAMESPACE);
        Integer attempt = store.get(ATTEMPT, Integer.class);

        String label = "Attempt " + (attempt == null ? 1 : attempt);

        Path videoPath = store.get(VIDEO_PATH, Path.class);
        Path tracePath = store.get(TRACE_PATH, Path.class);

        AllureArtifacts.attachFile(videoPath, label + " - Test Video", "video/webm");
        AllureArtifacts.attachFile(tracePath, label + " - Test Trace", "application/zip");
    }


    public static Path getTracePath(ExtensionContext context) {
        return context.getStore(NAMESPACE).get(TRACE_PATH, Path.class);
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext,  ExtensionContext extensionContext) {
        Class<?> type = parameterContext.getParameter().getType();
        return type == ExtensionContext.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return extensionContext;
    }
}

