package ru.adk.config;

@SuppressWarnings("SameParameterValue")
class GroovyConfigLoadingException extends RuntimeException {
    GroovyConfigLoadingException(String message) {
        super(message);
    }
}