package ru.art.tarantoolRefactored.configuration;

import java.util.Map;

import static ru.art.core.factory.CollectionsFactory.*;

public class TarantoolModuleConfiguration {
    public Map<String, TarantoolInstanceConfiguration> instances = mapOf();

}
