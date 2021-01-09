package io.art.model.configurator;

import io.art.model.implementation.value.*;
import static io.art.core.factory.SetFactory.*;
import java.lang.reflect.*;
import java.util.*;

public class ValueModelConfigurator {
    private final Set<Type> customTypes = set();

    public  ValueModelConfigurator model(Type type) {
        customTypes.add(type);
        return this;
    }

    ValueModuleModel configure() {
        return new ValueModuleModel(immutableSetOf(customTypes));
    }

}
