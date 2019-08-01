package ru.art.service.validation;

public interface Validatable {
    default void onValidating(Validator validator) {
    }
}
