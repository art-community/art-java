package io.art.storage.service;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import lombok.*;

@Public
@RequiredArgsConstructor
public class Index1Service<ModelType, F1> {
    private final IndexService<ModelType> delegate;

    public ModelType findFirst(F1 key1) {
        return delegate.findFirst(key1);
    }

    public ImmutableArray<ModelType> findAll(F1 key1) {
        return delegate.findAll(key1);
    }

    public ImmutableArray<ModelType> delete(F1 key1) {
        return delegate.delete(key1);
    }

    public long count() {
        return delegate.count();
    }
}
