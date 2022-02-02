package io.art.tarantool.model;

import io.art.core.annotation.*;
import io.art.tarantool.constants.TarantoolModuleConstants.*;
import lombok.*;
import lombok.experimental.*;

@Public
@Builder
@Getter
@Accessors(fluent = true)
public class TarantoolSpaceConfiguration {
    private final String name;
    private final Boolean ifNotExists;
    private final TarantoolFormatConfiguration format;
    private final Engine engine;
    private final Integer fieldCount;
    private final Integer id;
    private final Boolean local;
    private final Boolean sync;
    private final Boolean temporary;
    private final String user;
}
