package ru.art.entity.mapper;

import ru.art.entity.*;

public interface ValueFromModelMapper<T, V extends Value> {
    V map(T model);

    interface EntityFromModelMapper<T> extends ValueFromModelMapper<T, Entity> {
    }

    interface CollectionFromModelMapper<T> extends ValueFromModelMapper<T, CollectionValue<?>> {
    }

    interface PrimitiveFromModelMapper<T> extends ValueFromModelMapper<T, Primitive> {
    }

    interface StringDataPrimitiveFromModelMapper extends ValueFromModelMapper<String, Primitive> {
    }

    interface StringParametersMapFromModelMapper<T> extends ValueFromModelMapper<T, StringParametersMap> {
    }

    interface XmlEntityFromModelMapper<T> extends ValueFromModelMapper<T, XmlEntity> {
    }
}