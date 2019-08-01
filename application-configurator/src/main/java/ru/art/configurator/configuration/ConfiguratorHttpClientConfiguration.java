package ru.art.configurator.configuration;

import lombok.Getter;
import ru.art.http.client.configuration.HttpClientModuleConfiguration.HttpClientModuleDefaultConfiguration;
import ru.art.http.mapper.HttpContentMapper;
import ru.art.http.mime.MimeType;
import static ru.art.configurator.http.content.mapping.ConfiguratorHttpContentMapping.configureContentMappers;
import java.util.Map;

@Getter
public class ConfiguratorHttpClientConfiguration extends HttpClientModuleDefaultConfiguration {
    private final Map<MimeType, HttpContentMapper> contentMappers = configureContentMappers(super.getContentMappers());
}