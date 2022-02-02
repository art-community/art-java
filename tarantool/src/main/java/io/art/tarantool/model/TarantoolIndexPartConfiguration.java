package io.art.tarantool.model;

import io.art.core.annotation.*;
import io.art.tarantool.constants.TarantoolModuleConstants.*;
import lombok.*;
import lombok.experimental.*;

@Public
@Builder
@Getter
@Accessors(fluent = true)
public class TarantoolIndexPartConfiguration {
    private final int field;
    private final FieldType type;
    private final Boolean nullable;
    private final String path;
}
