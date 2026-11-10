package com.prestashop.test.runner;

import com.prestashop.test.base.PlaywrightSettings;
import com.prestashop.test.base.TestRunConfig;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider;

import java.util.List;
import java.util.stream.Stream;

public final class CustomRunnerExtension implements TestTemplateInvocationContextProvider {

    @Override
    public boolean supportsTestTemplate(ExtensionContext context) {
        return context.getTestMethod()
                .map(method -> method.isAnnotationPresent(CustomRunnerTest.class))
                .orElse(false);
    }

    @Override
    @NullMarked
    public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext context) {
        return PlaywrightSettings.testRuns()
                .stream()
                .map(BrowserInvocation::new);
    }

    private record BrowserInvocation(TestRunConfig run)  implements TestTemplateInvocationContext {

        @Override
        @NullMarked
        public String getDisplayName(int invocationIndex) {
            return run.toString();
        }

        @Override
        @NullMarked
        public List<Extension> getAdditionalExtensions() {
            return List.of(new BrowserInvocationExtension(run));
        }
    }
}
