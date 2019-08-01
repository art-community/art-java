package ru.art.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import static ru.art.entity.constants.ValueType.XmlValueType;
import static ru.art.entity.constants.ValueType.XmlValueType.CDATA;

@Getter
@AllArgsConstructor
public class CdataXmlValue implements XmlValue<XmlEntity> {
    private final XmlValueType type = CDATA;
    private XmlEntity value;
}
