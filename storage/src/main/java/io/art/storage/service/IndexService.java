package io.art.storage.service;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import static io.art.core.constants.CompilerSuppressingWarnings.*;
import static java.util.Arrays.*;
import java.util.*;

@Public
@SuppressWarnings({VARARGS})
public interface IndexService<ModelType> {
    default ModelType findFirst(Object... keys) {
        return findFirst(asList(keys));
    }

    ModelType findFirst(Collection<Object> keys);

    ModelType findFirst(ImmutableCollection<Object> keys);

    default ImmutableArray<ModelType> findAll(Object... keys) {
        return findAll(asList(keys));
    }

    ImmutableArray<ModelType> findAll(Collection<Object> keys);

    ImmutableArray<ModelType> findAll(ImmutableCollection<Object> keys);

    default ImmutableArray<ModelType> delete(Object... keys) {
        return delete(asList(keys));
    }

    ImmutableArray<ModelType> delete(Collection<Object> keys);

    ImmutableArray<ModelType> delete(ImmutableCollection<Object> keys);

    long count();

    ReactiveIndexService<ModelType> reactive();
}
