package ru.art.generator.spec.common.annotation;

import static ru.art.core.constants.StringConstants.EMPTY_STRING;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for specification generator.
 */
@Target(ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Listen {
    String httpServiceMethodPath() default EMPTY_STRING;

    String soapServiceListeningPath() default EMPTY_STRING;
}
