package ru.adk.http.server.context;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import ru.adk.http.mime.MimeType;
import java.util.Map;

@Getter
@Builder
public class MultiPartContext {
    @Singular
    private final Map<String, String> headers;
    private final MimeType contentType;
    private final MimeType acceptType;
    private final boolean hasContent;
    private final long contentLength;
    private final String fileName;
}
