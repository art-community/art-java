package ru.art.entity.mapper;

import ru.art.entity.*;

public interface ValueToModelMapper<T, V extends Value> {
    T map(V value);

    interface EntityToModelMapper<T> extends ValueToModelMapper<T, Entity> {
    }

    interface CollectionValueToModelMapper<T> extends ValueToModelMapper<T, CollectionValue<?>> {
    }

    interface PrimitiveToModelMapper<T> extends ValueToModelMapper<T, Primitive> {
    }

    interface StringDataPrimitiveToModelMapper<T> extends ValueToModelMapper<T, Primitive> {
    }

    interface StringParametersMapToModelMapper<T> extends ValueToModelMapper<T, StringParametersMap> {
    }

    interface XmlEntityToModelMapper<T> extends ValueToModelMapper<T, XmlEntity> {
    }
}
