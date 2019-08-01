package ru.adk.http.mapper;

import ru.adk.entity.Value;
import ru.adk.http.exception.HttpTextMapperException;
import ru.adk.http.mapper.HttpContentMapper.HttpContentToValueMapper;
import ru.adk.http.mapper.HttpContentMapper.HttpEntityToContentMapper;
import ru.adk.http.mime.MimeType;
import static java.util.Objects.isNull;
import static ru.adk.core.checker.CheckerForEmptiness.isEmpty;
import static ru.adk.core.constants.ArrayConstants.EMPTY_BYTES;
import static ru.adk.entity.PrimitivesFactory.stringPrimitive;
import static ru.adk.http.constants.HttpExceptionsMessages.CONTENT_TYPE_IS_NULL;
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
