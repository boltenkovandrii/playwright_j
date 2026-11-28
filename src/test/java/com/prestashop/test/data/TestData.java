package com.prestashop.test.data;

/**
 * Central repository for demo credentials and product constants used across tests.
 */
public final class TestData {

    /** Demo email for testing */
    public static final String DEMO_EMAIL = "pub@prestashop.com";

    /** Demo password for testing */
    public static final String DEMO_PASSWORD = "123456789";

    /** Demo email for backoffice testing */
    public static final String DEMO_BACKOFFICE_EMAIL = "demo@prestashop.com";

    /** Demo password for backoffice testing */
    public static final String DEMO_BACKOFFICE_PASSWORD = "prestashop_demo";

    /** Product name used in tests */
    public static final String PRODUCT_T_SHIRT = "Hummingbird printed t-shirt";

    private TestData() {
        // Prevent instantiation
    }

}
