package io.art.tarantool.model;

import io.art.core.annotation.*;
import io.art.meta.model.*;
import io.art.tarantool.constants.TarantoolModuleConstants.*;
import io.art.tarantool.exception.*;
import lombok.*;
import lombok.experimental.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.Errors.*;
import static java.text.MessageFormat.*;

@Public
@Builder
@Getter
@Accessors(fluent = true)
public class TarantoolIndexPartConfiguration {
    private final int field;
    private final FieldType type;
    private final Boolean nullable;
    private final String path;

    public static <C, M extends MetaClass<C>> TarantoolIndexPartConfigurationBuilder indexPartFor(MetaField<M, ?> field) {
        return TarantoolIndexPartConfiguration.builder()
                .field(field.owner().index(cast(field)) + 1)
                .type(extractType(field.type()));
    }

    private static FieldType extractType(MetaType<?> type) {
        switch (type.externalKind()) {
            case LAZY:
                return extractType(orElse(type.arrayComponentType(), () -> type.parameters().get(0)));
            case STRING:
            case CHARACTER:
                return FieldType.STRING;
            case LONG:
            case BYTE:
            case SHORT:
            case INTEGER:
                return FieldType.UNSIGNED;
            case DOUBLE:
                return FieldType.DOUBLE;
            case FLOAT:
                return FieldType.DECIMAL;
            case BOOLEAN:
                return FieldType.BOOLEAN;
            default:
                throw new TarantoolException(format(INVALID_TYPE_FOR_INDEX_PART, type));
        }
    }
}
