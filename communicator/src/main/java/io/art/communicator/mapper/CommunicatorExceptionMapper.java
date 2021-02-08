package io.art.communicator.mapper;

import io.art.value.immutable.Value;
import io.art.value.mapper.*;
import lombok.*;
import static io.art.value.constants.ValueModuleConstants.Fields.*;
import static io.art.value.immutable.Value.*;
import static io.art.value.mapping.ThrowableMapping.*;
import static lombok.AccessLevel.*;
import java.util.function.*;

@Getter
@AllArgsConstructor(access = PRIVATE)
public class CommunicatorExceptionMapper {
    private final Function<Value, Boolean> filter;
    private final ValueToModelMapper<? extends Throwable, ? extends Value> mapper;

    public static CommunicatorExceptionMapper communicatorExceptionMapper(Function<Value, Boolean> filter, ValueToModelMapper<? extends Throwable, ? extends Value> mapper) {
        return new CommunicatorExceptionMapper(filter, mapper);
    }

    public static CommunicatorExceptionMapper communicatorThrowableExceptionMapper() {
        Function<Value, Boolean> filter = value -> isEntity(value) && asEntity(value).has(EXCEPTION_KEY);
        ValueToModelMapper<Throwable, Value> mapper = value -> toThrowableNested(asEntity(value));
        return communicatorExceptionMapper(filter, mapper);
    }
}
