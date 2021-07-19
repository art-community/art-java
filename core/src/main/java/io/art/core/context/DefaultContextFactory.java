package io.art.core.context;

import io.art.core.configuration.*;
import io.art.core.singleton.*;
import lombok.experimental.*;
import static io.art.core.collection.ImmutableSet.*;
import static io.art.core.constants.EmptyFunctions.*;
import static io.art.core.context.Context.*;
import static io.art.core.singleton.SingletonAction.*;

@UtilityClass
public class DefaultContextFactory {
    private final static SingletonAction initialize = singletonAction();

    public static void initialize() {
        initialize.run(DefaultContextFactory::initializeDefault);
    }

    private static void initializeDefault() {
        prepareInitialization(ContextConfiguration.builder().build(), emptyConsumer());
        processInitialization(emptyImmutableSet());
    }
}
