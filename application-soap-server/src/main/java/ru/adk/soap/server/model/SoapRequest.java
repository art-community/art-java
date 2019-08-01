package ru.adk.soap.server.model;

import lombok.Builder;
import lombok.Getter;
import ru.adk.entity.XmlEntity;

@Builder
@Getter
public class SoapRequest {
    private final String operationId;
    private final XmlEntity entity;
}
