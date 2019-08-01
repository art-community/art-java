package ru.art.http.server.context;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import ru.art.http.mime.MimeType;
import javax.servlet.http.Cookie;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

@Getter
@Builder
public class HttpRequestContext {
    @Singular
    private final Map<String, String> headers;
    @Singular
    private final List<Cookie> cookies;
    @Singular
    private final Map<String, MultiPartContext> parts;

    private final String acceptLanguages;
    private final String acceptEncodings;
    private final Charset acceptCharset;
    private final String requestUrl;
    private final MimeType contentType;
    private final MimeType acceptType;
    private final boolean hasContent;
    private final int contentLength;
    private String interceptedResponseContentType;
}
