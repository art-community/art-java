package ru.art.soap.server.model;

import lombok.Builder;
import lombok.Getter;
import ru.art.entity.XmlEntity;

@Builder
@Getter
public class SoapRequest {
    private final String operationId;
    private final XmlEntity entity;
}
