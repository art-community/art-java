package ru.adk.http.json;

import ru.adk.entity.Value;
import ru.adk.http.mapper.HttpContentMapper.HttpContentToValueMapper;
import ru.adk.http.mapper.HttpContentMapper.HttpEntityToContentMapper;
import ru.adk.http.mime.MimeType;
import static java.util.Objects.isNull;
import static ru.adk.core.checker.CheckerForEmptiness.isEmpty;
import static ru.adk.core.constants.ArrayConstants.EMPTY_BYTES;
import static ru.adk.http.constants.HttpExceptionsMessages.CONTENT_TYPE_IS_NULL;
import static ru.adk.json.descriptor.JsonEntityReader.readJson;
import static ru.adk.json.descriptor.JsonEntityWriter.writeJson;
import java.nio.charset.Charset;

public class HttpJsonMapper implements HttpEntityToContentMapper, HttpContentToValueMapper {
    @Override
    public byte[] mapToBytes(Value value, MimeType mimeType, Charset charset) {
        if (isNull(mimeType)) throw new HttpJsonMapperException(CONTENT_TYPE_IS_NULL);
        if (isNull(value)) return EMPTY_BYTES;
        return writeJson(value).getBytes(charset);
    }

    @Override
    public Value mapFromBytes(byte[] content, MimeType mimeType, Charset charset) {
        if (isNull(mimeType)) throw new HttpJsonMapperException(CONTENT_TYPE_IS_NULL);
        if (isEmpty(content)) return null;
        String jsonString = new String(content, charset);
        return readJson(jsonString);
    }
}
