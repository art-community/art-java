package io.art.configurator.custom;

import io.art.core.source.*;

public interface CustomConfigurationProxy<T> {
    T configure(ConfigurationSource source);
}
