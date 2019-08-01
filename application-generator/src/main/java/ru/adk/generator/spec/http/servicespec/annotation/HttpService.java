package ru.adk.generator.spec.http.servicespec.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for specification generator.
 * Class which is marked with HttpService annotation needs HttpServiceSpecification to generate.
 */
@Target(ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface HttpService {
    String serve();
}
