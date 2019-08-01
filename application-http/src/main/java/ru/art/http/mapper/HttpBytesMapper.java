package ru.art.http.mapper;

import ru.art.entity.CollectionValuesFactory;
import ru.art.entity.Value;
import ru.art.http.exception.HttpTextMapperException;
import ru.art.http.mime.MimeType;
import static java.util.Objects.isNull;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.core.constants.ArrayConstants.EMPTY_BYTES;
import static ru.art.entity.CollectionValuesFactory.emptyCollection;
import static ru.art.entity.Value.asCollection;
import static ru.art.entity.Value.isCollection;
import static ru.art.http.constants.HttpExceptionsMessages.CONTENT_TYPE_IS_NULL;
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
