package ru.adk.config.extensions.http;

import ru.adk.core.factory.CollectionsFactory.MapBuilder;
import ru.adk.http.json.HttpJsonMapper;
import ru.adk.http.mapper.HttpBytesMapper;
import ru.adk.http.mapper.HttpContentMapper;
import ru.adk.http.mapper.HttpTextPlainMapper;
import ru.adk.http.mime.MimeType;
import ru.adk.http.xml.HttpXmlMapper;
import static ru.adk.core.factory.CollectionsFactory.mapOf;
import static ru.adk.http.constants.HttpMimeTypes.*;
import static ru.adk.metrics.http.constants.MetricsModuleHttpConstants.METRICS_CONTENT_TYPE;
import java.util.Map;

public interface HttpContentMappersConfigurator {
    static Map<MimeType, HttpContentMapper> configureHttpContentMappers(Map<MimeType, HttpContentMapper> parentMappers) {
        HttpTextPlainMapper textPlainMapper = new HttpTextPlainMapper();
        HttpJsonMapper jsonMapper = new HttpJsonMapper();
        HttpXmlMapper xmlMapper = new HttpXmlMapper();
        HttpContentMapper bytesContentMapper = new HttpContentMapper(new HttpBytesMapper(), textPlainMapper);
        HttpContentMapper textContentMapper = new HttpContentMapper(textPlainMapper, textPlainMapper);
        HttpContentMapper jsonContentMapper = new HttpContentMapper(jsonMapper, jsonMapper);
        HttpContentMapper xmlContentMapper = new HttpContentMapper(xmlMapper, xmlMapper);
        MapBuilder<MimeType, HttpContentMapper> mappers = mapOf(TEXT_HTML, textContentMapper)
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
                .add(TEXT_XML, xmlContentMapper)
                .add(TEXT_XML_UTF_8, xmlContentMapper)
                .add(TEXT_XML_WIN_1251, xmlContentMapper)
                .add(APPLICATION_XML, xmlContentMapper)
                .add(APPLICATION_SOAP_XML, xmlContentMapper)
                .add(METRICS_CONTENT_TYPE.getMimeType(), textContentMapper)
                .add(ALL, textContentMapper);
        mappers.putAll(parentMappers);
        return mappers;
    }
}
