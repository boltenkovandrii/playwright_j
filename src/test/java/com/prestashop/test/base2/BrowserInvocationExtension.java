package com.prestashop.test.base2;

import com.microsoft.playwright.Page;

import com.prestashop.test.base.AllureArtifacts;
import com.prestashop.test.base.TestRunConfig;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.LifecycleMethodExecutionExceptionHandler;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.junit.jupiter.api.extension.TestWatcher;

public final class BrowserInvocationExtension implements
        ParameterResolver,
        BeforeEachCallback,
        AfterEachCallback,
        TestExecutionExceptionHandler,
        LifecycleMethodExecutionExceptionHandler,
        TestWatcher {

    private final TestRunConfig run;

    private BrowserSession session;
    private boolean testFailed = false;

    public BrowserInvocationExtension(TestRunConfig run) {
        this.run = run;
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        int currentAttempt = RetryAttemptTracker.nextAttempt(context, run);
        AllureArtifacts.setAttempt(currentAttempt > 1);
        session = BrowserSession.start(run, currentAttempt);
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext,  ExtensionContext extensionContext) {
        Class<?> type = parameterContext.getParameter().getType();
        return type == Page.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        if (session == null) {
            throw new ParameterResolutionException("Browser session is not available");
        }
        return session.page();
    }

    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        testFailed = true;
        throw throwable;
    }

    @Override
    public void handleBeforeEachMethodExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        testFailed = true;
        throw throwable;
    }

    @Override
    public void handleAfterEachMethodExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        testFailed = true;
        throw throwable;
    }

    @Override
    public void afterEach(ExtensionContext context) {
        try {
            if (session == null) {
                return;
            }

            if (testFailed) {
                session.finishFailed();
            } else {
                session.finishPassed();
            }
        } finally {
            session = null;
            AllureArtifacts.clearAttempt();
        }
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        RetryAttemptTracker.markPassed(context, run);
    }
}