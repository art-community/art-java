package ru.adk.http.xml;

import ru.adk.entity.Value;
import ru.adk.http.exception.HttpTextMapperException;
import ru.adk.http.mime.MimeType;
import static java.util.Objects.isNull;
import static ru.adk.core.checker.CheckerForEmptiness.isEmpty;
import static ru.adk.core.constants.ArrayConstants.EMPTY_BYTES;
import static ru.adk.entity.Value.asXmlEntity;
import static ru.adk.entity.XmlEntity.xmlEntityBuilder;
import static ru.adk.entity.constants.ValueType.XML_ENTITY;
import static ru.adk.http.constants.HttpExceptionsMessages.CONTENT_TYPE_IS_NULL;
import static ru.adk.http.mapper.HttpContentMapper.HttpContentToValueMapper;
import static ru.adk.http.mapper.HttpContentMapper.HttpEntityToContentMapper;
import static ru.adk.http.xml.HttpXmlMapperConstants.HTTP_XML_MAPPER_SUPPORT_ONLY_XML_ENTITIES;
import static ru.adk.xml.descriptor.XmlEntityReader.readXml;
import static ru.adk.xml.descriptor.XmlEntityWriter.writeXml;
import static ru.adk.xml.module.XmlModule.xmlModule;
import java.nio.charset.Charset;

public class HttpXmlMapper implements HttpContentToValueMapper, HttpEntityToContentMapper {
    @Override
    public Value mapFromBytes(byte[] bytes, MimeType mimeType, Charset charset) {
        if (isNull(mimeType)) throw new HttpTextMapperException(CONTENT_TYPE_IS_NULL);
        if (isEmpty(bytes)) return xmlEntityBuilder().create();
        return readXml(xmlModule().getXmlInputFactory(), new String(bytes, charset));
    }

    @Override
    public byte[] mapToBytes(Value value, MimeType mimeType, Charset charset) {
        if (isNull(mimeType)) throw new HttpTextMapperException(CONTENT_TYPE_IS_NULL);
        if (isEmpty(value)) return EMPTY_BYTES;
        if (value.getType() != XML_ENTITY) throw new HttpXmlMapperException(HTTP_XML_MAPPER_SUPPORT_ONLY_XML_ENTITIES);
        return writeXml(xmlModule().getXmlOutputFactory(), asXmlEntity(value)).getBytes(charset);
    }
}
