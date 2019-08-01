package ru.adk.http.mapper;

import ru.adk.entity.CollectionValuesFactory;
import ru.adk.entity.Value;
import ru.adk.http.exception.HttpTextMapperException;
import ru.adk.http.mime.MimeType;
import static java.util.Objects.isNull;
import static ru.adk.core.checker.CheckerForEmptiness.isEmpty;
import static ru.adk.core.constants.ArrayConstants.EMPTY_BYTES;
import static ru.adk.entity.CollectionValuesFactory.emptyCollection;
import static ru.adk.entity.Value.asCollection;
import static ru.adk.entity.Value.isCollection;
import static ru.adk.http.constants.HttpExceptionsMessages.CONTENT_TYPE_IS_NULL;
import java.nio.charset.Charset;

public class HttpBytesMapper implements HttpContentMapper.HttpEntityToContentMapper, HttpContentMapper.HttpContentToValueMapper {
    @Override
    public byte[] mapToBytes(Value value, MimeType mimeType, Charset charset) {
        if (isNull(mimeType)) throw new HttpTextMapperException(CONTENT_TYPE_IS_NULL);
        if (isNull(value)) return EMPTY_BYTES;
        if (!isCollection(value)) return EMPTY_BYTES;
        return asCollection(value).getByteArray();
    }

    @Override
    public Value mapFromBytes(byte[] content, MimeType mimeType, Charset charset) {
        if (isNull(mimeType)) throw new HttpTextMapperException(CONTENT_TYPE_IS_NULL);
        if (isNull(charset)) return emptyCollection();
        if (isEmpty(content)) return emptyCollection();
        return CollectionValuesFactory.byteCollection(content);
    }
}
