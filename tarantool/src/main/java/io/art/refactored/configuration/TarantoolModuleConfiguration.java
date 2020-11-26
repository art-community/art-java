package io.art.refactored.configuration;

import io.art.core.module.ModuleConfiguration;
import io.art.core.module.ModuleConfigurator;
import io.art.core.source.ConfigurationSource;
import lombok.RequiredArgsConstructor;

import static io.art.refactored.constants.TarantoolModuleConstants.ConfigurationKeys.*;
import static java.util.Optional.*;

import java.util.Map;

import static io.art.core.factory.CollectionsFactory.mapOf;

public class TarantoolModuleConfiguration implements ModuleConfiguration {
    public Map<String, TarantoolInstanceConfiguration> instances = mapOf();

    @RequiredArgsConstructor
    public static class Configurator implements ModuleConfigurator<TarantoolModuleConfiguration, Configurator>{
        private final TarantoolModuleConfiguration configuration;


    }
}

/*
tarantool_refactored:
    instances:

 */