package io.art.storage.registry;

import io.art.core.lazy.LazyValue;
import io.art.storage.space.Space;

import java.util.Map;
import java.util.function.Supplier;

import static io.art.core.factory.MapFactory.map;
import static io.art.core.lazy.LazyValue.lazy;

public class StorageSpacesRegistry {
    private final Map<String, LazyValue<Space<?,?>>> spaces = map();

    public void register(String id, Supplier<Space<?, ?>> supplier){
        spaces.put(id, lazy(supplier));
    }

    public Space<?,?> get(String id){
        return spaces.get(id).get();
    }
}
