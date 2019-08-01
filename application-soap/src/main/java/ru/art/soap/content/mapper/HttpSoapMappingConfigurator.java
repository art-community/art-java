package ru.art.soap.content.mapper;

import ru.art.http.mapper.HttpContentMapper;
import ru.art.http.mime.MimeType;
import ru.art.http.xml.HttpXmlMapper;
import static ru.art.core.factory.CollectionsFactory.mapOf;
import static ru.art.http.constants.HttpMimeTypes.*;
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
