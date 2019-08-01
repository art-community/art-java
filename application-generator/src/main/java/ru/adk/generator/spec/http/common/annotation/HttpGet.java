package ru.adk.generator.spec.http.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static ru.adk.core.constants.StringConstants.EMPTY_STRING;

/**
 * Annotation for specification generator.
 */
@Target(ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface HttpGet {
    String httpService() default EMPTY_STRING;

    boolean httpProxy() default false;
}
