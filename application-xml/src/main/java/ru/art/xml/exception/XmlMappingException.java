package ru.art.xml.exception;

public class XmlMappingException extends RuntimeException {
    public XmlMappingException(String message) {
        super(message);
    }

    public XmlMappingException(Exception e) {
        super(e);
    }
}
