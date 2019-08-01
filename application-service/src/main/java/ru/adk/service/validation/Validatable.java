package ru.adk.service.validation;

public interface Validatable {
    default void onValidating(Validator validator) {
    }
}
