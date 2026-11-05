package com.prestashop.test.base2;


import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.TestTemplate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@TestTemplate
@ExtendWith(CustomRunnerExtension.class)
public @interface CustomRunnerTest {
}