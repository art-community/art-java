package io.art.configurator.custom;

import io.art.core.source.*;

public interface CustomConfigurator<T> {
    T configure(ConfigurationSource source);
}
