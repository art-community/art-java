package ru.art.service.validation;

public abstract class ValidationExpression<T> {
    protected String fieldName;
    protected T value;

    public boolean evaluate(String fieldName, T value) {
        this.fieldName = fieldName;
        this.value = value;
        return value != null;
    }

    public abstract String getValidationErrorMessage();
}
