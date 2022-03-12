package io.art.storage.service;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import lombok.*;

@Public
@RequiredArgsConstructor
public class Index3Service<ModelType, F1, F2, F3> {
    private final IndexService<ModelType> delegate;

    public ModelType findFirst(F1 key1, F2 key2, F3 key3) {
        return delegate.findFirst(key1, key2, key3);
    }

    public ImmutableArray<ModelType> findAll(F1 key1, F2 key2, F3 key3) {
        return delegate.findAll(key1, key2, key3);
    }

    public ImmutableArray<ModelType> delete(F1 key1, F2 key2, F3 key3) {
        return delegate.delete(key1, key2, key3);
    }

    public long count() {
        return delegate.count();
    }
}
