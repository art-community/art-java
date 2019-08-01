package ru.art.generator.mapper.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for mapper generator.
 * Fields marked with GenerationException annotation contains parameters
 * that haven't been generated.
 * Variable notGeneratedFields contains names of fields that haven't been generated.
 */
@Target(ElementType.FIELD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface GenerationException {
    String notGeneratedFields();
}
