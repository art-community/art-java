package io.art.tarantool.model;

import io.art.core.annotation.*;
import lombok.*;
import lombok.experimental.*;

@Public
@Builder
@Getter
@Accessors(fluent = true)
public class TarantoolIndexConfiguration {
    private final String spaceName;
    private final String indexName;
}
