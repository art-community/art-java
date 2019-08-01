package ru.adk.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import static ru.adk.entity.constants.ValueType.XmlValueType;
import static ru.adk.entity.constants.ValueType.XmlValueType.STRING;

@Getter
@AllArgsConstructor
public class StringXmlValue implements XmlValue<String> {
    private final XmlValueType type = STRING;
    private String value;
}
