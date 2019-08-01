package ru.adk.http.server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import ru.adk.entity.mapper.ValueFromModelMapper;
import ru.adk.entity.mapper.ValueToModelMapper;
import ru.adk.http.constants.HttpMethodType;
import ru.adk.http.constants.HttpRequestDataSource;
import ru.adk.http.constants.MimeToContentTypeMapper;
import ru.adk.http.server.builder.HttpServiceBuilder;
import ru.adk.http.server.builder.HttpServiceBuilderImplementation;
import ru.adk.http.server.constants.HttpServerModuleConstants.HttpResponseHandlingMode;
import ru.adk.http.server.interceptor.HttpServerInterceptor;
import ru.adk.http.server.path.HttpPath;
import ru.adk.service.constants.RequestValidationPolicy;
import static ru.adk.http.server.constants.HttpServerModuleConstants.HttpResponseHandlingMode.CHECKED;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class HttpService {
    private final String path;
    private final List<HttpMethod> httpMethods;
    private final List<HttpServerInterceptor> requestInterceptors;
    private final List<HttpServerInterceptor> responseInterceptors;

    public static HttpServiceBuilder httpService() {
        return new HttpServiceBuilderImplementation();
    }

    @Getter
    @Builder
    public static class HttpMethod {
        private final String methodId;
        private final HttpPath path;
        private final HttpMethodType methodType;
        private final HttpRequestDataSource requestDataSource;
        private final ValueToModelMapper requestMapper;
        private final ValueFromModelMapper responseMapper;
        private final ValueFromModelMapper exceptionMapper;
        private final List<HttpServerInterceptor> requestInterceptors;
        private final List<HttpServerInterceptor> responseInterceptors;
        private final RequestValidationPolicy requestValidationPolicy;
        private final MimeToContentTypeMapper consumesContentType;
        private final MimeToContentTypeMapper producesContentType;
        private final boolean ignoreRequestAcceptType;
        private final boolean ignoreRequestContentType;
        private final boolean overrideResponseContentType;
        @Builder.Default
        private final HttpResponseHandlingMode responseHandlingMode = CHECKED;
    }

    @Getter
    @Builder
    public static class HttpMethodGroup {
        private Map<HttpMethodType, HttpMethod> methods;
    }
}