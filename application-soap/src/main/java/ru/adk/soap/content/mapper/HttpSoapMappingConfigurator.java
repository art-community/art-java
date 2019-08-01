package ru.adk.soap.content.mapper;

import ru.adk.http.mapper.HttpContentMapper;
import ru.adk.http.mime.MimeType;
import ru.adk.http.xml.HttpXmlMapper;
import static ru.adk.core.factory.CollectionsFactory.mapOf;
import static ru.adk.http.constants.HttpMimeTypes.*;
import java.util.Map;

public interface HttpSoapMappingConfigurator {
    static Map<MimeType, HttpContentMapper> configureSoapHttpMappers(Map<MimeType, HttpContentMapper> defaultMappers) {
        HttpXmlMapper httpXmlMapper = new HttpXmlMapper();
        HttpContentMapper httpSoapMapper = new HttpContentMapper(httpXmlMapper, httpXmlMapper);
        return mapOf(defaultMappers)
                .add(APPLICATION_SOAP_XML, httpSoapMapper)
                .add(APPLICATION_XML, httpSoapMapper)
                .add(TEXT_XML, httpSoapMapper);
    }

}
