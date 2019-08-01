package ru.adk.json.exception;

public class JsonMappingException extends RuntimeException {
    public JsonMappingException(String message) {
        super(message);
    }

    public JsonMappingException(Exception e) {
        super(e);
    }
}
