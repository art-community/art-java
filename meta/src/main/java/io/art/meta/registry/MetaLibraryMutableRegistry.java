package io.art.meta.registry;

import io.art.core.collection.*;
import io.art.meta.model.*;
import static io.art.core.factory.ArrayFactory.*;
import static io.art.core.factory.MapFactory.*;
import java.util.*;
import java.util.function.*;

public class MetaLibraryMutableRegistry {
    private final static Map<Class<?>, Supplier<MetaLibrary>> REGISTRY = map();

    public static ImmutableArray<Supplier<MetaLibrary>> get() {
        return immutableArrayOf(REGISTRY.values());
    }

    public static void clear() {
        REGISTRY.clear();
    }

    public static void registerMetaLibrary(Class<? extends MetaLibrary> libraryClass, Supplier<MetaLibrary> library) {
        REGISTRY.putIfAbsent(libraryClass, library);
    }
}
