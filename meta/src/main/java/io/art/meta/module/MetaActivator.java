package io.art.meta.module;

import io.art.core.module.*;
import io.art.meta.model.*;
import lombok.experimental.*;
import static io.art.core.constants.ModuleIdentifiers.*;
import static io.art.core.module.ModuleActivator.*;
import java.util.function.*;

@UtilityClass
public class MetaActivator {
    public static ModuleActivator meta(Supplier<? extends MetaLibrary> factory) {
        MetaLibrary library = factory.get();
        library.compute();
        return module(META_MODULE_ID, () -> new MetaModule(library));
    }
}
