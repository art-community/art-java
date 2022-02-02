package io.art.tarantool.model;

import io.art.core.annotation.*;
import io.art.tarantool.constants.TarantoolModuleConstants.*;
import lombok.*;
import lombok.experimental.*;
import java.util.*;

@Public
@Builder
@Getter
@Accessors(fluent = true)
public class TarantoolFormatConfiguration {
    @Singular("format")
    private final Map<String, FieldType> format;
}
