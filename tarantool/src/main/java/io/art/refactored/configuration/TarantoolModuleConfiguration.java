package io.art.refactored.configuration;

import java.util.Map;

import static io.art.core.factory.CollectionsFactory.mapOf;

public class TarantoolModuleConfiguration {
    public Map<String, TarantoolInstanceConfiguration> instances = mapOf();

}
