package io.art.meta.module;

import io.art.core.module.*;
import io.art.meta.model.*;
import lombok.experimental.*;
import static io.art.core.constants.ModuleIdentifiers.*;
import static io.art.core.module.ModuleActivator.*;
import java.util.function.*;

@UtilityClass
public class MetaActivator {
    public ModuleActivator meta(Supplier<? extends MetaLibrary> factory) {
        return module(META_MODULE_ID, () -> new MetaModule(factory.get()));
    }

    public ModuleActivator meta(Supplier<? extends MetaLibrary> factory, UnaryOperator<MetaInitializer> initializer) {
        MetaLibrary library = factory.get();
        return module(META_MODULE_ID, () -> new MetaModule(library), () -> initializer.apply(new MetaInitializer(library)));
    }
}
