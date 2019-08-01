package ru.adk.soap.server.model;

import lombok.Builder;
import lombok.Getter;
import ru.adk.entity.XmlEntity;

@Builder
@Getter
public class SoapResponse {
    private final XmlEntity xmlEntity;
}
