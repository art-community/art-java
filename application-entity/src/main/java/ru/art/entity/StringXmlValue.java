package ru.art.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import static ru.art.entity.constants.ValueType.XmlValueType;
import static ru.art.entity.constants.ValueType.XmlValueType.STRING;

@Getter
@AllArgsConstructor
public class StringXmlValue implements XmlValue<String> {
    private final XmlValueType type = STRING;
    private String value;
}
