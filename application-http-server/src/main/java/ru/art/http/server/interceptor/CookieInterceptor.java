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

import lombok.*;
import ru.art.core.constants.*;
import ru.art.http.server.exception.*;
import static java.util.Arrays.*;
import static java.util.Objects.*;
import static ru.art.core.checker.CheckerForEmptiness.*;
import static ru.art.core.constants.InterceptionStrategy.*;
import static ru.art.core.context.Context.*;
import static ru.art.http.constants.HttpHeaders.*;
import static ru.art.http.constants.HttpMethodType.*;
import static ru.art.http.constants.HttpMimeTypes.*;
import javax.servlet.http.*;
import java.util.*;
import java.util.function.*;

@Builder
public class CookieInterceptor implements HttpServerInterception {
    @Singular("path")
    private final Set<String> paths;
    @Singular("cookie")
    private final Map<String, Supplier<String>> cookies;
    private final Function<String, Error> errorProvider;

    @Override
    public InterceptionStrategy intercept(HttpServletRequest request, HttpServletResponse response) {
        if (paths.stream().noneMatch(url -> request.getRequestURI().contains(url)) ||
                request.getMethod().equals(OPTIONS.name()) ||
                hasTokenCookie(request)) {
            return NEXT_INTERCEPTOR;
        }
        try {
            if (isNull(errorProvider)) {
                return NEXT_INTERCEPTOR;
            }
            response.setCharacterEncoding(ifEmpty(request.getCharacterEncoding(), contextConfiguration().getCharset().name()));
            response.setHeader(CONTENT_TYPE, TEXT_HTML_UTF_8.toString());
            Error error = errorProvider.apply(request.getRequestURI());
            response.setStatus(error.status);
            if (isNotEmpty(error.content)) {
                response.getOutputStream().write(error.content.getBytes());
            }
            response.getOutputStream().close();
            return NEXT_INTERCEPTOR;
        } catch (Throwable e) {
            throw new HttpServerException(e);
        }
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
        Supplier<String> supplier = cookies.get(cookie.getName());
        if (isNull(supplier)) return false;
        return cookie.getValue().equalsIgnoreCase(supplier.get());
    }

    @Getter
    @AllArgsConstructor(staticName = "cookieError")
    public static class Error {
        private final int status;
        private final String content;
    }
}
