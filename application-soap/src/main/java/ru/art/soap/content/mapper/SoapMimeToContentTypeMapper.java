package ru.art.soap.content.mapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.http.entity.ContentType;
import ru.art.http.constants.HttpMimeTypes;
import ru.art.http.constants.MimeToContentTypeMapper;
import ru.art.http.mime.MimeType;
import static lombok.AccessLevel.PRIVATE;

@Getter
@AllArgsConstructor(access = PRIVATE)
public class SoapMimeToContentTypeMapper {
    private MimeType mimeType;
    private ContentType contentType;

    public static SoapMimeToContentTypeMapper textXml() {
        return new SoapMimeToContentTypeMapper(HttpMimeTypes.TEXT_XML, ContentType.TEXT_XML);
    }

    public static SoapMimeToContentTypeMapper applicationXml() {
        return new SoapMimeToContentTypeMapper(HttpMimeTypes.APPLICATION_XML, ContentType.APPLICATION_XML);
    }

    public static SoapMimeToContentTypeMapper applicationSoapXml() {
        return new SoapMimeToContentTypeMapper(HttpMimeTypes.APPLICATION_SOAP_XML, ContentType.APPLICATION_XML);
    }

    public MimeToContentTypeMapper toHttpMimeToContentTypeMapper() {
        return new MimeToContentTypeMapper(mimeType, contentType);
    }
}
