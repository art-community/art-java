package ru.adk.http.server;

import ru.adk.http.constants.HttpHeaders;
import ru.adk.http.mapper.HttpContentMapper;
import ru.adk.http.server.context.HttpRequestContext;
import ru.adk.http.server.handler.HttpExceptionHandler;
import ru.adk.service.exception.ServiceExecutionException;
import static java.text.MessageFormat.format;
import static java.util.Objects.isNull;
import static ru.adk.core.context.Context.contextConfiguration;
import static ru.adk.core.extension.NullCheckingExtensions.getOrElse;
import static ru.adk.http.constants.HttpMimeTypes.APPLICATION_JSON_UTF8;
import static ru.adk.http.constants.HttpStatus.INTERNAL_SERVER_ERROR;
import static ru.adk.http.server.body.descriptor.HttpBodyDescriptor.writeResponseBody;
import static ru.adk.http.server.constants.HttpExceptionResponses.SERVICE_EXCEPTION_HANDLING_ERROR_RESPONSE;
import static ru.adk.http.server.module.HttpServerModule.httpServerModule;
import static ru.adk.http.server.module.HttpServerModule.httpServerModuleState;
import static ru.adk.service.mapping.ServiceEntitiesMapping.ServiceExecutionExceptionMapping.serviceExecutionExceptionMapper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;

class ServiceHttpJsonExceptionHandler implements HttpExceptionHandler<ServiceExecutionException> {
    @Override
    public void handle(ServiceExecutionException exception, HttpServletRequest request, HttpServletResponse response) {
        response.setHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_UTF8.toString());
        HttpRequestContext requestContext = httpServerModuleState().getRequestContext();
        Charset charset = isNull(requestContext) ? contextConfiguration().getCharset() : getOrElse(requestContext.getAcceptCharset(), contextConfiguration().getCharset());
        try {
            response.setStatus(INTERNAL_SERVER_ERROR.getCode());
            HttpContentMapper contentMapper = httpServerModule().getContentMappers().get(APPLICATION_JSON_UTF8);
            byte[] bodyBytes = contentMapper.getToContent().mapToBytes(serviceExecutionExceptionMapper.map(exception), APPLICATION_JSON_UTF8, charset);
            writeResponseBody(response, bodyBytes);
        } catch (Exception e) {
            String error = format(SERVICE_EXCEPTION_HANDLING_ERROR_RESPONSE, exception.getErrorCode(), exception.getErrorMessage());
            writeResponseBody(response, error.getBytes(charset));
        }
    }
}
