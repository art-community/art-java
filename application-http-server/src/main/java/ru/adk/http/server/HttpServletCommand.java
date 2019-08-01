package ru.adk.http.server;

import lombok.Builder;
import lombok.Getter;
import ru.adk.http.constants.MimeToContentTypeMapper;
import ru.adk.http.server.model.HttpService;

@Getter
@Builder
class HttpServletCommand {
    private final String path;
    private final MimeToContentTypeMapper consumesContentType;
    private final MimeToContentTypeMapper producesContentType;
    private final boolean ignoreRequestContentType;
    private final boolean ignoreRequestAcceptType;
    private final HttpService.HttpMethod httpMethod;
    private final String serviceId;
}
