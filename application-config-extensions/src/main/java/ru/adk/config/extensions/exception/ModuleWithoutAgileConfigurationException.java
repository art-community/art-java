package ru.adk.config.extensions.exception;

public class ModuleWithoutAgileConfigurationException extends RuntimeException {
    public ModuleWithoutAgileConfigurationException(String message) {
        super(message);
    }
}
