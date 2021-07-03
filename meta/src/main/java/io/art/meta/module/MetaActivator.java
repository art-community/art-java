package io.art.meta.module;

import io.art.core.module.*;
import io.art.meta.model.*;
import lombok.experimental.*;
import static io.art.core.module.ModuleActivator.*;
import java.util.function.*;

@UtilityClass
public class MetaActivator {
    public static ModuleActivator meta(Supplier<MetaLibrary> factory) {
        factory.get().compute();
        return module(MetaModule.class, MetaModule::new);
    }
}
