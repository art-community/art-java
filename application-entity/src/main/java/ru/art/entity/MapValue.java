package ru.art.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;
import ru.art.core.checker.CheckerForEmptiness;
import ru.art.entity.constants.ValueType;
import java.util.Map;

@Builder
@Getter
@EqualsAndHashCode
public class MapValue implements Value {
    private final ValueType type = ValueType.MAP;
    @Singular
    private final Map<? extends Value, ? extends Value> elements;

    @Override
    public String toString() {
        return elements.toString();
    }

    @Override
    public boolean isEmpty() {
        return CheckerForEmptiness.isEmpty(elements);
    }
}
