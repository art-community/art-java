package ru.adk.http.server.interceptor;

import ru.adk.core.constants.InterceptionStrategy;
import static ru.adk.core.constants.InterceptionStrategy.NEXT_INTERCEPTOR;
import static ru.adk.http.constants.HttpHeaders.ORIGIN;
import static ru.adk.http.server.constants.HttpServerModuleConstants.HttpWebUiServiceConstants.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HttpWebInterception implements HttpServerInterception {
    @Override
    public InterceptionStrategy intercept(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN_KEY, request.getHeader(ORIGIN));
        response.setHeader(ACCESS_CONTROL_ALLOW_METHODS_KEY, ACCESS_CONTROL_ALLOW_METHODS_VALUE);
        response.setHeader(ACCESS_CONTROL_ALLOW_HEADERS_KEY, ACCESS_CONTROL_ALLOW_HEADERS_VALUE);
        response.setHeader(ACCESS_CONTROL_MAX_AGE_HEADERS_KEY, ACCESS_CONTROL_MAX_AGE_HEADERS_VALUE);
        response.setHeader(ACCESS_CONTROL_ALLOW_CREDENTIALS_KEY, ACCESS_CONTROL_ALLOW_CREDENTIALS_VALUE);
        return NEXT_INTERCEPTOR;
    }
}
