package ru.adk.metrics.http.configurator;

import ru.adk.core.factory.CollectionsFactory;
import ru.adk.http.mapper.HttpContentMapper;
import ru.adk.http.mapper.HttpTextPlainMapper;
import ru.adk.http.mime.MimeType;
import static ru.adk.core.factory.CollectionsFactory.mapOf;
import static ru.adk.metrics.http.constants.MetricsModuleHttpConstants.METRICS_CONTENT_TYPE;
import java.util.Map;

public interface HttpMetricsMapperConfigurator {
    static Map<MimeType, HttpContentMapper> configureMetricsContentMapper(Map<MimeType, HttpContentMapper> currentMappers) {
        HttpTextPlainMapper textPlainMapper = new HttpTextPlainMapper();
        CollectionsFactory.MapBuilder<MimeType, HttpContentMapper> mappers = mapOf(currentMappers);
        mappers.put(METRICS_CONTENT_TYPE.getMimeType(), new HttpContentMapper(textPlainMapper, textPlainMapper));
        return mappers;
    }
}
