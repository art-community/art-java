package io.art.meta.module;

import io.art.core.module.*;
import io.art.meta.model.*;
import lombok.experimental.*;
import static io.art.core.module.ModuleActivator.*;
import java.util.function.*;

@UtilityClass
public class MetaActivator {
    public static ModuleActivator meta(Supplier<? extends MetaLibrary> factory) {
        MetaLibrary library = factory.get();
        library.compute();
        return module(MetaModule.class, () -> new MetaModule(library));
    }
}
