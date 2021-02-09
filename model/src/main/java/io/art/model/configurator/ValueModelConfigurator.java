package io.art.model.configurator;

import io.art.model.implementation.value.*;
import static io.art.core.factory.SetFactory.*;
import java.lang.reflect.*;
import java.util.*;

public class ValueModelConfigurator {
    private final Set<Type> mappedTypes = set();

    public  ValueModelConfigurator mapping(Type type) {
        mappedTypes.add(type);
        return this;
    }

    ValueModuleModel configure() {
        return new ValueModuleModel(immutableSetOf(mappedTypes));
    }

}
