package ru.art.metrics.http.configurator;

import ru.art.core.factory.CollectionsFactory;
import ru.art.http.mapper.HttpContentMapper;
import ru.art.http.mapper.HttpTextPlainMapper;
import ru.art.http.mime.MimeType;
import static ru.art.core.factory.CollectionsFactory.mapOf;
import static ru.art.metrics.http.constants.MetricsModuleHttpConstants.METRICS_CONTENT_TYPE;
import java.util.Map;

public interface HttpMetricsMapperConfigurator {
    static Map<MimeType, HttpContentMapper> configureMetricsContentMapper(Map<MimeType, HttpContentMapper> currentMappers) {
        HttpTextPlainMapper textPlainMapper = new HttpTextPlainMapper();
        CollectionsFactory.MapBuilder<MimeType, HttpContentMapper> mappers = mapOf(currentMappers);
        mappers.put(METRICS_CONTENT_TYPE.getMimeType(), new HttpContentMapper(textPlainMapper, textPlainMapper));
        return mappers;
    }
}
