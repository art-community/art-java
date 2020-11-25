package ru.art.refactored.configuration;

import java.util.Map;
import java.util.Optional;

import static ru.art.core.factory.CollectionsFactory.*;

public class TarantoolModuleConfiguration {
    public Map<String, TarantoolInstanceConfiguration> instances = mapOf();

}
