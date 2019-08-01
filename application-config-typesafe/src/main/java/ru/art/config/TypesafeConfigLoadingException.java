package ru.art.config;

class TypesafeConfigLoadingException extends RuntimeException {
    TypesafeConfigLoadingException(String message) {
        super(message);
    }

    TypesafeConfigLoadingException(Exception e) {
        super(e);
    }
}
