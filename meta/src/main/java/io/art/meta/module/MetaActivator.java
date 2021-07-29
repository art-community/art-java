package io.art.meta.module;

import io.art.core.module.*;
import io.art.core.property.*;
import io.art.meta.model.*;
import lombok.experimental.*;
import static io.art.core.constants.ModuleIdentifiers.*;
import static io.art.core.module.ModuleActivator.*;
import static io.art.core.property.LazyProperty.*;
import java.util.function.*;

@UtilityClass
public class MetaActivator {
    public ModuleActivator meta(Supplier<? extends MetaLibrary> factory) {
        LazyProperty<? extends MetaLibrary> library = lazy(factory);
        return module(META_MODULE_ID, () -> new MetaModule(library));
    }

    public ModuleActivator meta(Supplier<? extends MetaLibrary> factory, UnaryOperator<MetaInitializer> initializer) {
        LazyProperty<? extends MetaLibrary> library = lazy(factory);
        return module(META_MODULE_ID, () -> new MetaModule(library), () -> initializer.apply(new MetaInitializer(library)));
    }
}
