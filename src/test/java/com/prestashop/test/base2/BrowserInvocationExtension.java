package com.prestashop.test.base2;

import com.microsoft.playwright.Page;

import com.prestashop.test.base.TestRunConfig;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;

public final class BrowserInvocationExtension implements
        ParameterResolver,
        BeforeEachCallback,
        AfterEachCallback,
        TestExecutionExceptionHandler {

    private final TestRunConfig run;

    private BrowserSession session;
    private Throwable testFailure;

    public BrowserInvocationExtension(TestRunConfig run) {
        this.run = run;
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        testFailure = null;

        session = BrowserSession.start(run, 1);
    }

    @Override
    public boolean supportsParameter(
            ParameterContext parameterContext,
            ExtensionContext extensionContext) {

        Class<?> type = parameterContext.getParameter().getType();
        return type == TestRunConfig.class || type == Page.class;
    }

    @Override
    public Object resolveParameter(
            ParameterContext parameterContext,
            ExtensionContext extensionContext) {

        Class<?> type = parameterContext.getParameter().getType();

        if (type == TestRunConfig.class) {
            return run;
        }

        if (type == Page.class && session != null) {
            return session.page();
        }

        throw new ParameterResolutionException(
                "Unable to resolve parameter: " + type.getName());
    }

    @Override
    public void handleTestExecutionException(
            ExtensionContext context,
            Throwable throwable) throws Throwable {

        testFailure = throwable;
        throw throwable;
    }

    @Override
    public void afterEach(ExtensionContext context) {
        if (session == null) {
            return;
        }

        try {
            if (testFailure == null) {
                session.finishPassed();
            } else {
                session.finishFailed();
            }
        } finally {
            session = null;
        }
    }
}