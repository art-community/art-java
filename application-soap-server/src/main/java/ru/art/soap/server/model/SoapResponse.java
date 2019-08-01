package ru.art.soap.server.model;

import lombok.Builder;
import lombok.Getter;
import ru.art.entity.XmlEntity;

@Builder
@Getter
public class SoapResponse {
    private final XmlEntity xmlEntity;
}
