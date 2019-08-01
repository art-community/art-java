package ru.art.entity.mapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.art.entity.Value;
import ru.art.entity.exception.ValueMappingException;
import static java.util.Objects.isNull;
import static lombok.AccessLevel.PRIVATE;
import static ru.art.entity.constants.ValueMappingExceptionMessages.FROM_MODULE_MAPPER_IS_NULL;
import static ru.art.entity.constants.ValueMappingExceptionMessages.TO_MODULE_MAPPER_IS_NULL;

@Getter
@AllArgsConstructor(access = PRIVATE)
public class ValueMapper<T, V extends Value> {
    private final ValueFromModelMapper<T, V> fromModel;
    private final ValueToModelMapper<T, V> toModel;

    public static <T, V extends Value> ValueMapper<T, V> mapper(ValueFromModelMapper<T, V> fromModel, ValueToModelMapper<T, V> toModel) {
        if (isNull(fromModel)) throw new ValueMappingException(FROM_MODULE_MAPPER_IS_NULL);
        if (isNull(toModel)) throw new ValueMappingException(TO_MODULE_MAPPER_IS_NULL);
        return new ValueMapper<>(fromModel, toModel);
    }

    public T map(V value) {
        return toModel.map(value);
    }

    public V map(T value) {
        return fromModel.map(value);
    }
}
