package io.art.storage.registry;

import io.art.core.property.*;
import io.art.storage.space.*;

import java.util.*;
import java.util.function.*;

import static io.art.core.factory.MapFactory.*;
import static io.art.core.property.LazyProperty.*;

public class StorageSpacesRegistry {
    private final Map<String, LazyProperty<Space<?,?>>> spaces = map();

    public void register(String id, Supplier<Space<?, ?>> supplier){
        spaces.put(id, lazy(supplier));
    }

    public Space<?,?> get(String id){
        return spaces.get(id).get();
    }
}
