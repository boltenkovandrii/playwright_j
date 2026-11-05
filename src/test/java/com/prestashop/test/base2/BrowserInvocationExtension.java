package com.prestashop.test.base2;

import com.microsoft.playwright.Page;

import com.prestashop.test.base.AllureArtifacts;
import com.prestashop.test.base.TestRunConfig;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.junit.jupiter.api.extension.TestWatcher;
import org.opentest4j.TestAbortedException;

public final class BrowserInvocationExtension implements
        ParameterResolver,
        BeforeEachCallback,
        AfterEachCallback,
        TestExecutionExceptionHandler,
        TestWatcher {

    private final TestRunConfig run;

    private BrowserSession session;
    private Throwable testFailure;

    public BrowserInvocationExtension(TestRunConfig run) {
        this.run = run;
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        if (RetryAttemptTracker.hasPassed(context, run)) {
            throw new TestAbortedException("Configuration already passed in a previous attempt: " + run);
        }
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

        Class<?> type = parameterContext.getParameter().getType();

        if (type == Page.class && session != null) {
            return session.page();
        }

        throw new ParameterResolutionException("Unable to resolve parameter: " + type.getName());
    }

    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {

        testFailure = throwable;
        throw throwable;
    }

    @Override
    public void afterEach(ExtensionContext context) {
        try {
            if (session == null) {
                return;
            }

            if (testFailure == null) {
                session.finishPassed();
            } else {
                session.finishFailed();
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