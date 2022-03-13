package io.art.storage.service;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.core.model.*;
import static io.art.core.constants.CompilerSuppressingWarnings.*;
import static java.util.Arrays.*;
import java.util.*;

@Public
@SuppressWarnings({VARARGS})
public interface IndexService<ModelType> {
    ModelType first(Tuple tuple);

    ImmutableArray<ModelType> select(Tuple tuple);

    ImmutableArray<ModelType> select(Tuple tuple, int offset, int limit);

    default ImmutableArray<ModelType> find(Tuple... keys) {
        return find(asList(keys));
    }

    ImmutableArray<ModelType> find(Collection<? extends Tuple> keys);

    ImmutableArray<ModelType> find(ImmutableCollection<? extends Tuple> keys);

    ModelType delete(Tuple key);

    default ImmutableArray<ModelType> delete(Tuple... keys) {
        return delete(asList(keys));
    }

    ImmutableArray<ModelType> delete(Collection<? extends Tuple> keys);

    ImmutableArray<ModelType> delete(ImmutableCollection<? extends Tuple> keys);

    long count(Tuple tuple);

    ReactiveIndexService<ModelType> reactive();
}
