package ru.art.entity;

import static ru.art.entity.constants.ValueType.XmlValueType;

public interface XmlValue<T> {
    T getValue();

    XmlValueType getType();
}

