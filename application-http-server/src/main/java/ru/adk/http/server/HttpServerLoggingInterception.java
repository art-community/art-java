package ru.adk.http.server;

import ru.adk.core.constants.InterceptionStrategy;
import ru.adk.http.server.interceptor.HttpServerInterception;
import ru.adk.logging.ProtocolCallLoggingParameters;
import static java.lang.System.getProperty;
import static java.util.UUID.randomUUID;
import static ru.adk.core.checker.CheckerForEmptiness.isEmpty;
import static ru.adk.core.checker.CheckerForEmptiness.isNotEmpty;
import static ru.adk.core.constants.InterceptionStrategy.NEXT_INTERCEPTOR;
import static ru.adk.core.constants.StringConstants.EMPTY_STRING;
import static ru.adk.http.server.constants.HttpServerModuleConstants.*;
import static ru.adk.logging.LoggingParametersManager.clearProtocolLoggingParameters;
import static ru.adk.logging.LoggingParametersManager.putProtocolCallLoggingParameters;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

public class HttpServerLoggingInterception implements HttpServerInterception {
    @Override
    public InterceptionStrategy intercept(HttpServletRequest request, HttpServletResponse response) {
        Enumeration<String> traceIdHeader = request.getHeaders(TRACE_ID_HEADER);
        Enumeration<String> profileHeader = request.getHeaders(PROFILE_HEADER);
        clearProtocolLoggingParameters();
        putProtocolCallLoggingParameters(ProtocolCallLoggingParameters.builder()
                .protocol(request.getScheme())
                .requestId(randomUUID().toString())
                .traceId(isEmpty(traceIdHeader) || !traceIdHeader.hasMoreElements() ? randomUUID().toString() : traceIdHeader.nextElement())
                .profile(extractProfile(profileHeader))
                .environment(getProperty(ENVIRONMENT_PROPERTY))
                .build());
        return NEXT_INTERCEPTOR;
    }

    private String extractProfile(Enumeration<String> profileHeaders) {
        if (isNotEmpty(profileHeaders) && profileHeaders.hasMoreElements()) {
            return profileHeaders.nextElement();
        }
        return EMPTY_STRING;
    }
}
