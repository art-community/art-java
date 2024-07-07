package io.art.meta.model;

import io.art.core.annotation.*;
import io.art.core.collection.*;

import static io.art.core.caster.Caster.cast;
import static io.art.core.factory.MapFactory.*;
import java.util.*;
import java.util.function.*;

@Generation
public abstract class MetaProxy {
    private final Map<MetaMethod<MetaClass<?>, ?>, Function<Object, Object>> invocations;

    protected void run(Function<Object, Object> function) {
        function.apply(null);
    }

    protected <T> T get(Function<Object, Object> function) {
        return cast(function.apply(null));
    }

    protected <T> T single(Function<Object, Object> function, Object argument) {
        return cast(function.apply(argument));
    }

    protected <T> T multiple(Function<Object, Object> function, Object[] arguments) {
        return cast(function.apply(cast(arguments)));
    }

    public MetaProxy(Map<MetaMethod<MetaClass<?>, ?>, Function<Object, Object>> invocations) {
        this.invocations = invocations;
    }

    public ImmutableMap<MetaMethod<MetaClass<?>, ?>, Function<Object, Object>> invocations() {
        return immutableMapOf(invocations);
    }
}
