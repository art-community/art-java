package ru.adk.generator.spec.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for specification generator.
 */
@Target(ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface NotNull {
    boolean httpSpec() default false;

    boolean soapSpec() default false;

    boolean grpcSpec() default false;
}
