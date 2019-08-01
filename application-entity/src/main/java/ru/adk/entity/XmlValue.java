package ru.adk.entity;

import static ru.adk.entity.constants.ValueType.XmlValueType;

public interface XmlValue<T> {
    T getValue();

    XmlValueType getType();
}

