package ru.adk.soap.server.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SoapFault extends RuntimeException {
    private String codeValue;
    private String reasonText;
}
