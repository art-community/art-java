package io.art.tarantool.model;

import io.art.core.annotation.*;
import lombok.*;
import lombok.experimental.*;

@Public
@Builder
@Getter
@Accessors(fluent = true)
public class TarantoolSpaceConfiguration {
    private final String name;
    private final boolean ifNotExists;
    private final TarantoolFormatConfiguration format;
}
