package ru.adk.configurator.configuration;

import lombok.Getter;
import ru.adk.http.client.configuration.HttpClientModuleConfiguration.HttpClientModuleDefaultConfiguration;
import ru.adk.http.mapper.HttpContentMapper;
import ru.adk.http.mime.MimeType;
import static ru.adk.configurator.http.content.mapping.ConfiguratorHttpContentMapping.configureContentMappers;
import java.util.Map;

@Getter
public class ConfiguratorHttpClientConfiguration extends HttpClientModuleDefaultConfiguration {
    private final Map<MimeType, HttpContentMapper> contentMappers = configureContentMappers(super.getContentMappers());
}