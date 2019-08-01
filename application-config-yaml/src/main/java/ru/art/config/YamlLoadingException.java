package ru.art.config;

@SuppressWarnings("SameParameterValue")
class YamlLoadingException extends RuntimeException {
    YamlLoadingException(Throwable cause) {
        super(cause);
    }

    YamlLoadingException(String message) {
        super(message);
    }
}
