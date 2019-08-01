package ru.adk.entity.tuple.schema;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import ru.adk.entity.Value;
import ru.adk.entity.constants.ValueType;
import static java.util.Objects.isNull;
import static ru.adk.core.checker.CheckerForEmptiness.isEmpty;
import static ru.adk.core.factory.CollectionsFactory.dynamicArrayOf;
import java.util.List;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class ValueSchema {
    private final ValueType type;

    public static ValueSchema fromValue(Value value) {
        if (isNull(value)) {
            return null;
        }

        if (Value.isPrimitive(value)) {
            return new ValueSchema(value.getType());
        }

        switch (value.getType()) {
            case ENTITY:
                return new EntitySchema(Value.asEntity(value));
            case COLLECTION:
                return new CollectionValueSchema(Value.asCollection(value), Value.asCollection(value).getElementsType());
            case STRING_PARAMETERS_MAP:
                return new StringParametersSchema(Value.asStringParametersMap(value));
            case MAP:
                return new MapValueSchema(Value.asMap(value));
        }
        return null;
    }

    public List<?> toTuple() {
        return dynamicArrayOf(type.name());
    }

    public static ValueSchema fromTuple(List<?> tuple) {
        if (isEmpty(tuple)) {
            return null;
        }

        if (tuple.size() == 1) {
            return new ValueSchema(ValueType.valueOf((String) tuple.get(0)));
        }

        ValueType valueType = ValueType.valueOf((String) tuple.get(0));
        if (Value.isPrimitiveType(valueType)) {
            return new ValueSchema(valueType);
        }

        switch (valueType) {
            case ENTITY:
                return EntitySchema.fromTuple(tuple);
            case COLLECTION:
                return CollectionValueSchema.fromTuple(tuple);
            case MAP:
                return MapValueSchema.fromTuple(tuple);
            case STRING_PARAMETERS_MAP:
                return StringParametersSchema.fromTuple(tuple);
        }

        return null;
    }
}
