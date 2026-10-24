package com.prestashop.test.base;


import com.prestashop.test.utils.AllureReporting;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestBase {

    AllureReporting allureReporting = new AllureReporting();

    @BeforeAll
    public void beforeAll() {

    }

    public AllureReporting getReporting() {
        return this.allureReporting;
    }

}
