package ru.adk.configurator.http.content.mapping;

import ru.adk.core.factory.CollectionsFactory;
import ru.adk.http.json.HttpJsonMapper;
import ru.adk.http.mapper.HttpBytesMapper;
import ru.adk.http.mapper.HttpContentMapper;
import ru.adk.http.mapper.HttpTextPlainMapper;
import ru.adk.http.mime.MimeType;
import static ru.adk.core.factory.CollectionsFactory.mapOf;
import static ru.adk.http.constants.HttpMimeTypes.*;
import static ru.adk.metrics.http.constants.MetricsModuleHttpConstants.METRICS_CONTENT_TYPE;
import java.util.Map;

public interface ConfiguratorHttpContentMapping {
    static Map<MimeType, HttpContentMapper> configureContentMappers(Map<MimeType, HttpContentMapper> parentMappers) {
        HttpTextPlainMapper textPlainMapper = new HttpTextPlainMapper();
        HttpJsonMapper jsonMapper = new HttpJsonMapper();
        HttpContentMapper bytesContentMapper = new HttpContentMapper(new HttpBytesMapper(), textPlainMapper);
        HttpContentMapper textContentMapper = new HttpContentMapper(textPlainMapper, textPlainMapper);
        HttpContentMapper jsonContentMapper = new HttpContentMapper(jsonMapper, jsonMapper);
        CollectionsFactory.MapBuilder<MimeType, HttpContentMapper> mappers = mapOf(TEXT_HTML, textContentMapper)
                .add(TEXT_HTML_UTF_8, textContentMapper)
                .add(TEXT_HTML_WIN_1251, textContentMapper)
                .add(IMAGE_JPEG, bytesContentMapper)
                .add(IMAGE_PNG, bytesContentMapper)
                .add(TEXT_CSV, bytesContentMapper)
                .add(APPLICATION_OCTET_STREAM, bytesContentMapper)
                .add(IMAGE_WEBP, bytesContentMapper)
                .add(APPLICATION_JSON, jsonContentMapper)
                .add(APPLICATION_JSON_UTF8, jsonContentMapper)
                .add(APPLICATION_JSON_WIN_1251, jsonContentMapper)
                .add(TEXT_PLAIN, textContentMapper)
                .add(METRICS_CONTENT_TYPE.getMimeType(), textContentMapper)
                .add(ALL, textContentMapper);
        mappers.putAll(parentMappers);
        return mappers;
    }
}
