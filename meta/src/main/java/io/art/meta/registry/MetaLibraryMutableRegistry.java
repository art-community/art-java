package io.art.meta.registry;

import io.art.core.collection.*;
import io.art.meta.model.*;
import static io.art.core.factory.ArrayFactory.*;
import static io.art.core.factory.ListFactory.*;
import java.util.*;
import java.util.function.*;

public class MetaLibraryMutableRegistry {
    private final static List<Supplier<MetaLibrary>> META_LIBRARY_REGISTRY = linkedList();

    public static ImmutableArray<Supplier<MetaLibrary>> get() {
        return immutableArrayOf(META_LIBRARY_REGISTRY);
    }

    public static void clear() {
        META_LIBRARY_REGISTRY.clear();
    }

    public static void registerMetaLibrary(Supplier<MetaLibrary> library) {
        META_LIBRARY_REGISTRY.add(library);
    }
}
