package io.art.storage.service;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import static io.art.core.constants.CompilerSuppressingWarnings.*;
import static java.util.Arrays.*;
import java.util.*;

@Public
@SuppressWarnings({UNCHECKED, VARARGS})
public interface IndexService<KeyType, ModelType> {
    ModelType findFirst(KeyType key);

    default ImmutableArray<ModelType> findAll(KeyType... keys) {
        return findAll(asList(keys));
    }

    ImmutableArray<ModelType> findAll(Collection<KeyType> keys);

    ImmutableArray<ModelType> findAll(ImmutableCollection<KeyType> keys);

    long count();

    ReactiveIndexService<KeyType, ModelType> reactive();
}
