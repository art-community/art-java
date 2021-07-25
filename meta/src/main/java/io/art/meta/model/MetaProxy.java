package io.art.meta.model;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import static io.art.core.factory.MapFactory.*;
import java.util.*;
import java.util.function.*;

@ForGenerator
public abstract class MetaProxy {
    private final Map<MetaMethod<?>, Function<Object, Object>> invocations;

    public MetaProxy(Map<MetaMethod<?>, Function<Object, Object>> invocations) {
        this.invocations = invocations;
    }

    public ImmutableMap<MetaMethod<?>, Function<Object, Object>> invocations() {
        return immutableMapOf(invocations);
    }
}
