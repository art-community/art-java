package ru.art.http.mapper;

import ru.art.entity.Value;
import ru.art.http.exception.HttpTextMapperException;
import ru.art.http.mapper.HttpContentMapper.HttpContentToValueMapper;
import ru.art.http.mapper.HttpContentMapper.HttpEntityToContentMapper;
import ru.art.http.mime.MimeType;
import static java.util.Objects.isNull;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.core.constants.ArrayConstants.EMPTY_BYTES;
import static ru.art.entity.PrimitivesFactory.stringPrimitive;
import static ru.art.http.constants.HttpExceptionsMessages.CONTENT_TYPE_IS_NULL;
import java.nio.charset.Charset;

public class HttpTextPlainMapper implements HttpEntityToContentMapper, HttpContentToValueMapper {
    @Override
    public byte[] mapToBytes(Value value, MimeType mimeType, Charset charset) {
        if (isNull(mimeType)) throw new HttpTextMapperException(CONTENT_TYPE_IS_NULL);
        if (isNull(value)) return EMPTY_BYTES;
        return value.toString().getBytes(charset);
    }

    @Override
    public Value mapFromBytes(byte[] content, MimeType mimeType, Charset charset) {
        if (isNull(mimeType)) throw new HttpTextMapperException(CONTENT_TYPE_IS_NULL);
        if (isEmpty(content)) return null;
        return stringPrimitive(new String(content, charset));
    }
}
