package ru.art.http.mapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.art.entity.Value;
import ru.art.http.mime.MimeType;
import java.nio.charset.Charset;

@Getter
@AllArgsConstructor
public class HttpContentMapper {
    private final HttpEntityToContentMapper toContent;
    private final HttpContentToValueMapper fromContent;

    public interface HttpEntityToContentMapper {
        byte[] mapToBytes(Value value, MimeType mimeType, Charset charset);
    }

    public interface HttpContentToValueMapper {
        Value mapFromBytes(byte[] content, MimeType mimeType, Charset charset);
    }
}
