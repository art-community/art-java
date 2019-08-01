package ru.adk.http.server.interceptor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.adk.core.constants.InterceptionStrategy;
import ru.adk.http.server.exception.HttpServerException;
import static java.util.Objects.isNull;
import static lombok.AccessLevel.PRIVATE;
import static ru.adk.core.constants.InterceptionStrategy.*;
import static ru.adk.http.server.constants.HttpServerExceptionMessages.INTERCEPTION_IS_NULL;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Getter
@AllArgsConstructor(access = PRIVATE)
public class HttpServerInterceptor {
    private final HttpServerInterception interception;
    private final InterceptionStrategy strategy;

    public static HttpServerInterceptor intercept(HttpServerInterception interception) {
        if (isNull(interception)) throw new HttpServerException(INTERCEPTION_IS_NULL);
        return new HttpServerInterceptor(interception, NEXT_INTERCEPTOR);
    }

    public static HttpServerInterceptor interceptAndProcessHandling(HttpServerInterception interception) {
        if (isNull(interception)) throw new HttpServerException(INTERCEPTION_IS_NULL);
        return new HttpServerInterceptor(interception, PROCESS_HANDLING);
    }

    public static HttpServerInterceptor interceptAndStopHandling(HttpServerInterception interception) {
        if (isNull(interception)) throw new HttpServerException(INTERCEPTION_IS_NULL);
        return new HttpServerInterceptor(interception, STOP_HANDLING);
    }


    public InterceptionStrategy intercept(HttpServletRequest request, HttpServletResponse response) {
        try {
            return interception.intercept(request, response);
        } catch (Exception e) {
            throw new HttpServerException(e);
        }
    }
}
