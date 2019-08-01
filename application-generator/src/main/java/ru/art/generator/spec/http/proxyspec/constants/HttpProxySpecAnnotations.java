package ru.art.generator.spec.http.proxyspec.constants;

import ru.art.generator.spec.http.proxyspec.exception.HttpProxySpecAnnotationDefinitionException;

import static java.text.MessageFormat.format;
import static ru.art.generator.spec.common.constants.SpecExceptionConstants.DefinitionExceptions.UNABLE_TO_DEFINE_ANNOTATION;

/**
 * Annotations for HttpProxyServiceSpecification.
 * Representing possible methods to execute for method-variables
 */
public enum HttpProxySpecAnnotations {
    REQUEST_MAPPER("RequestMapper"),
    RESPONSE_MAPPER("ResponseMapper"),
    FROM_BODY("FromBody"),
    FROM_PATH_PARAMS("FromPathParams"),
    FROM_QUERY_PARAMS("FromQueryParams"),
    HTTP_PROXY_CONSUMES("Consumes"),
    HTTP_PROXY_PRODUCES("Produces"),
    HTTP_PROXY_GET("HttpGet"),
    HTTP_PROXY_POST("HttpPost"),
    HTTP_PROXY_DELETE("HttpDelete"),
    HTTP_PROXY_HEAD("HttpHead"),
    HTTP_PROXY_OPTIONS("HttpOptions"),
    HTTP_PROXY_PATCH("HttpPatch"),
    HTTP_PROXY_PUT("HttpPut"),
    HTTP_PROXY_TRACE("HttpTrace"),
    HTTP_PROXY_CONNECT("HttpConnect"),
    REQUEST_INTERCEPTOR("RequestInterceptor"),
    RESPONSE_INTERCEPTOR("ResponseInterceptor"),
    METHOD_PATH("MethodPath"),
    HTTP_PROXY_SERVICE("HttpProxyService");

    private String className;

    public String getClassName() {
        return className;
    }

    HttpProxySpecAnnotations(String className) {
        this.className = className;
    }

    public static HttpProxySpecAnnotations parseAnnotationClassName(String annotationClassName) throws HttpProxySpecAnnotationDefinitionException {
        if (REQUEST_INTERCEPTOR.className.equals(annotationClassName)) return REQUEST_INTERCEPTOR;
        if (REQUEST_MAPPER.className.equals(annotationClassName)) return REQUEST_MAPPER;
        if (RESPONSE_MAPPER.className.equals(annotationClassName)) return RESPONSE_MAPPER;
        if (RESPONSE_INTERCEPTOR.className.equals(annotationClassName)) return RESPONSE_INTERCEPTOR;
        if (FROM_BODY.className.equals(annotationClassName)) return FROM_BODY;
        if (FROM_PATH_PARAMS.className.equals(annotationClassName)) return FROM_PATH_PARAMS;
        if (FROM_QUERY_PARAMS.className.equals(annotationClassName)) return FROM_QUERY_PARAMS;
        if (HTTP_PROXY_CONSUMES.className.equals(annotationClassName)) return HTTP_PROXY_CONSUMES;
        if (HTTP_PROXY_PRODUCES.className.equals(annotationClassName)) return HTTP_PROXY_PRODUCES;
        if (HTTP_PROXY_GET.className.equals(annotationClassName)) return HTTP_PROXY_GET;
        if (HTTP_PROXY_POST.className.equals(annotationClassName)) return HTTP_PROXY_POST;
        if (HTTP_PROXY_DELETE.className.equals(annotationClassName)) return HTTP_PROXY_DELETE;
        if (HTTP_PROXY_HEAD.className.equals(annotationClassName)) return HTTP_PROXY_HEAD;
        if (HTTP_PROXY_OPTIONS.className.equals(annotationClassName)) return HTTP_PROXY_OPTIONS;
        if (HTTP_PROXY_PATCH.className.equals(annotationClassName)) return HTTP_PROXY_PATCH;
        if (HTTP_PROXY_PUT.className.equals(annotationClassName)) return HTTP_PROXY_PUT;
        if (HTTP_PROXY_TRACE.className.equals(annotationClassName)) return HTTP_PROXY_TRACE;
        if (HTTP_PROXY_CONNECT.className.equals(annotationClassName)) return HTTP_PROXY_CONNECT;
        if (METHOD_PATH.className.equals(annotationClassName)) return METHOD_PATH;
        if (HTTP_PROXY_SERVICE.className.equals(annotationClassName)) return HTTP_PROXY_SERVICE;

        throw new HttpProxySpecAnnotationDefinitionException(format(UNABLE_TO_DEFINE_ANNOTATION, annotationClassName));
    }
}
