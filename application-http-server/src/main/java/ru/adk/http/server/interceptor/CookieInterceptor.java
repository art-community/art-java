package ru.adk.http.server.interceptor;

import lombok.Builder;
import lombok.Singular;
import ru.adk.core.constants.InterceptionStrategy;
import static java.util.Arrays.stream;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static ru.adk.core.checker.CheckerForEmptiness.isEmpty;
import static ru.adk.core.checker.CheckerForEmptiness.isNotEmpty;
import static ru.adk.core.constants.InterceptionStrategy.NEXT_INTERCEPTOR;
import static ru.adk.core.context.Context.contextConfiguration;
import static ru.adk.http.constants.HttpHeaders.CONTENT_TYPE;
import static ru.adk.http.constants.HttpMethodType.OPTIONS;
import static ru.adk.http.constants.HttpMimeTypes.TEXT_HTML_UTF_8;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

@Builder
public class CookieInterceptor implements HttpServerInterception {
    @Singular
    private final Set<String> checkingUrls;
    @Singular
    private final Map<String, Supplier<String>> cookieValues;
    private final int errorStatus;
    private final String errorContent;

    @Override
    public InterceptionStrategy intercept(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (checkingUrls.stream().noneMatch(url -> request.getRequestURI().contains(url)) || request.getMethod().equals(OPTIONS.name()) || hasTokenCookie(request)) {
            return NEXT_INTERCEPTOR;
        }
        response.setCharacterEncoding(contextConfiguration().getCharset().name());
        response.setHeader(CONTENT_TYPE, TEXT_HTML_UTF_8.toString());
        response.setStatus(errorStatus);
        if (isNotEmpty(errorContent)) {
            response.getOutputStream().write(errorContent.getBytes());
        }
        response.getOutputStream().close();
        return NEXT_INTERCEPTOR;
    }

    private boolean hasTokenCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (isEmpty(cookies)) return false;
        return stream(cookies)
                .filter(Objects::nonNull)
                .filter(cookie -> nonNull(cookie.getName()))
                .filter(cookie -> nonNull(cookie.getValue()))
                .anyMatch(this::filterCookie);
    }

    private boolean filterCookie(Cookie cookie) {
        Supplier<String> supplier = cookieValues.get(cookie.getName());
        if (isNull(supplier)) return false;
        return cookie.getValue().equalsIgnoreCase(supplier.get());
    }
}
