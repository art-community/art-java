package ru.adk.generator.spec.http.proxyspec.constants;

import ru.adk.generator.spec.http.proxyspec.operations.HttpProxySpecificationClassGenerator;

/**
 * Interface containing constants for {@link HttpProxySpecificationClassGenerator}
 */
public interface HttpProxySpecConstants {
    String PROXY_SPEC = "ProxySpec";
    String UNWRAP = "unwrap";
    String HTTP_CLIENT_PROXY_BY_URL_WITHOUT_PARAMS = "httpClientProxy(getUrl() + $S)";
    String HTTP_CLIENT_PROXY_BY_URL_WITH_PARAMS = "HttpClientProxyBuilder.<$L, $L> httpClientProxy(getUrl() + $S)";

    String EXEC_REQ_PROXY_SPEC = "case $L:\nreturn $N().withBody(($T) request).prepare().execute();\nreturn null";
    String EXEC_RESP_PROXY_SPEC = "case $L:\nreturn cast(unwrap($N().execute()))";
    String EXEC_RESP_AND_REQ_PROXY_SPEC = "case $L:\nreturn cast(unwrap($N().withBody(($T) request).prepare().execute()))";
    String EXEC_NO_RESP_OR_REQ_PROXY_SPEC = "case $L:\n$N().execute();\nreturn null";

    String TODO_NOT_GENERATED_REQ_IN_EXEC = "//TODO add request manually, change method's type to \"HttpClientProxyWithRequestBodyBuilder\" and add \".withBody()\" method in ";

    interface ErrorConstants {
        String METHOD_PATH_IS_EMPTY = "Method path in @MethodPath annotation is empty!";
        String UNABLE_TO_DEFINE_MIME_TYPE_METHOD = "Unable to define MimeToContentTypeMapper method for HttpMimeType {0}.";
        String UNABLE_TO_DEFINE_MIME_TYPE = "Unable to define HttpMimeType: {0}.";
        String PARAM_FROM_IS_MISSING = ".withReq() parameter was not generated because annotation is missing: @FromBody";
    }
}
