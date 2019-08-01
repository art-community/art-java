package ru.art.json.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import ru.art.core.module.ModuleConfiguration;

public interface JsonModuleConfiguration extends ModuleConfiguration {
    ObjectMapper getObjectMapper();

    class JsonModuleDefaultConfiguration implements JsonModuleConfiguration {
        @Getter
        private final ObjectMapper objectMapper = new ObjectMapper();
    }
}
