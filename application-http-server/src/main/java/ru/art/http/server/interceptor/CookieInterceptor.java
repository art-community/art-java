/*
 * ART Java
 *
 * Copyright 2019 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.art.http.server.interceptor;

import lombok.Builder;
import lombok.Singular;
import ru.art.core.constants.InterceptionStrategy;
import ru.art.http.server.exception.HttpServerException;
import static java.util.Arrays.stream;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.core.checker.CheckerForEmptiness.isNotEmpty;
import static ru.art.core.constants.InterceptionStrategy.NEXT_INTERCEPTOR;
import static ru.art.core.context.Context.contextConfiguration;
import static ru.art.http.constants.HttpHeaders.CONTENT_TYPE;
import static ru.art.http.constants.HttpMethodType.OPTIONS;
import static ru.art.http.constants.HttpMimeTypes.TEXT_HTML_UTF_8;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

@Builder
public class CookieInterceptor implements HttpServerInterception {
    @Singular("url")
    private final Set<String> urls;
    @Singular("cookie")
    private final Map<String, Supplier<String>> coockies;
    private final int errorStatus;
    private final String errorContent;

    @Override
    public InterceptionStrategy intercept(HttpServletRequest request, HttpServletResponse response) {
        if (urls.stream().noneMatch(url -> request.getRequestURI().contains(url)) || request.getMethod().equals(OPTIONS.name()) || hasTokenCookie(request)) {
            return NEXT_INTERCEPTOR;
        }
        response.setCharacterEncoding(contextConfiguration().getCharset().name());
        response.setHeader(CONTENT_TYPE, TEXT_HTML_UTF_8.toString());
        response.setStatus(errorStatus);
        try {
            if (isNotEmpty(errorContent)) {
                response.getOutputStream().write(errorContent.getBytes());
            }
            response.getOutputStream().close();
        } catch (Throwable e) {
            throw new HttpServerException(e);
        }
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
        Supplier<String> supplier = coockies.get(cookie.getName());
        if (isNull(supplier)) return false;
        return cookie.getValue().equalsIgnoreCase(supplier.get());
    }
}
