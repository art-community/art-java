package io.art.storage;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.meta.model.*;
import static io.art.core.constants.CompilerSuppressingWarnings.*;
import static java.util.Arrays.*;
import java.util.*;

@Public
@SuppressWarnings({UNCHECKED, VARARGS})
public interface SpaceService<KeyType, ModelType> {
    ModelType findFirst(KeyType key);

    default ImmutableArray<ModelType> findAll(KeyType... keys) {
        return findAll(asList(keys));
    }

    ImmutableArray<ModelType> findAll(Collection<KeyType> keys);

    ImmutableArray<ModelType> findAll(ImmutableCollection<KeyType> keys);

    ModelType delete(KeyType key);

    default ImmutableArray<ModelType> delete(KeyType... keys) {
        return delete(asList(keys));
    }

    ImmutableArray<ModelType> delete(Collection<KeyType> keys);

    ImmutableArray<ModelType> delete(ImmutableCollection<KeyType> keys);

    long count();

    void truncate();

    ModelType insert(ModelType value);

    default ImmutableArray<ModelType> insert(ModelType... value) {
        return insert(asList(value));
    }

    ImmutableArray<ModelType> insert(Collection<ModelType> value);

    ImmutableArray<ModelType> insert(ImmutableCollection<ModelType> value);

    ModelType put(ModelType value);

    default ImmutableArray<ModelType> put(ModelType... value) {
        return put(Arrays.asList(value));
    }

    ImmutableArray<ModelType> put(Collection<ModelType> value);

    ImmutableArray<ModelType> put(ImmutableCollection<ModelType> value);

    ModelType add(MetaField<MetaClass<ModelType>, ? extends Number> field, Number value);

    ModelType subtract(MetaField<MetaClass<ModelType>, ? extends Number> field, Number value);

    <FieldType> ModelType set(MetaField<MetaClass<ModelType>, FieldType> field, FieldType value);

    <FieldType> ModelType delete(MetaField<MetaClass<ModelType>, FieldType> field, FieldType value);

    SpaceStream<ModelType> stream();

    ReactiveSpaceService<KeyType, ModelType> reactive();
}
