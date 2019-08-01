package ru.art.generator.spec.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for specification generator.
 */
@Target(ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface RequestMapper {
    Class httpService() default Object.class;

    Class httpProxy() default Object.class;

    Class soapService() default Object.class;

    Class soapProxy() default Object.class;

    Class grpcService() default Object.class;

    Class grpcProxy() default Object.class;

    Class forAll() default Object.class;
}
