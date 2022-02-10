package io.art.meta.module;

import io.art.core.module.*;
import io.art.core.property.*;
import io.art.meta.model.*;
import lombok.experimental.*;
import static io.art.core.constants.ModuleIdentifiers.*;
import static io.art.core.module.ModuleActivator.*;
import static io.art.core.property.LazyProperty.*;
import static java.util.function.UnaryOperator.*;
import java.util.function.*;

@UtilityClass
public class MetaActivator {
    public ModuleActivator meta() {
        LazyProperty<EmptyMetaLibrary> library = lazy(() -> new EmptyMetaLibrary(new MetaLibrary[0]));
        return module(META_MODULE_ID, () -> new MetaModule(library), () -> new MetaInitializer(library));
    }

    public ModuleActivator meta(Supplier<? extends MetaLibrary> factory) {
        return meta(factory, identity());
    }

    public ModuleActivator meta(Supplier<? extends MetaLibrary> factory, UnaryOperator<MetaInitializer> initializer) {
        LazyProperty<? extends MetaLibrary> library = lazy(factory);
        return module(META_MODULE_ID, () -> new MetaModule(library), () -> initializer.apply(new MetaInitializer(library)));
    }
}
