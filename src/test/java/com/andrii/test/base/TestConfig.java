package com.andrii.test.base;


import org.apache.commons.configuration2.CompositeConfiguration;
import org.apache.commons.configuration2.EnvironmentConfiguration;
import org.apache.commons.configuration2.SystemConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

public final class TestConfig {

    private static final TestConfig INSTANCE = new TestConfig();

    private final CompositeConfiguration config;

    private TestConfig() {
        config = new CompositeConfiguration();
        config.addConfiguration(new SystemConfiguration());
        config.addConfiguration(new EnvironmentConfiguration());
        try {
            Configurations configs = new Configurations();
            config.addConfiguration(configs.properties("config.properties"));
        } catch (ConfigurationException e) {
            throw new IllegalStateException("Unable to load test configuration", e);
        }
    }

    public static CompositeConfiguration getConfiguration() {
        return INSTANCE.config;
    }

}
