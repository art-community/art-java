/*
 * ART
 *
 * Copyright 2020 ART
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

package io.art.http.client.interceptor;

import lombok.*;
import org.apache.http.*;
import org.apache.http.client.methods.*;
import io.art.core.constants.*;
import io.art.http.client.constants.*;
import io.art.http.client.exception.*;
import static java.util.Objects.*;
import static io.art.http.client.constants.HttpClientExceptionMessages.*;
import static io.art.http.client.interceptor.HttpClientRequestInterception.*;


@Getter
@Builder
public class HttpClientInterceptor {
    private HttpClientRequestInterception requestInterception;
    private HttpClientResponseInterception responseInterception;

    public static HttpClientInterceptor interceptWithHeader(String headerName, String headerValue) {
        return HttpClientInterceptor.builder()
                .requestInterception(interceptAndContinue(request -> request.addHeader(headerName, headerValue)))
                .build();
    }

    public static HttpClientInterceptor interceptRequest(HttpClientRequestInterception interception) {
        if (isNull(interception))
            throw new HttpClientException(HttpClientExceptionMessages.REQUEST_INTERCEPTION_IS_NULL);
        return HttpClientInterceptor.builder()
                .requestInterception(interception)
                .build();
    }

    public static HttpClientInterceptor interceptResponse(HttpClientResponseInterception interception) {
        if (isNull(interception)) throw new HttpClientException(RESPONSE_INTERCEPTION_IS_NULL);
        return HttpClientInterceptor.builder()
                .responseInterception(interception)
                .build();
    }

    public InterceptionStrategy interceptRequest(HttpUriRequest request) {
        return requestInterception.intercept(request);
    }

    public InterceptionStrategy interceptResponse(HttpUriRequest request, HttpResponse response) {
        return responseInterception.intercept(request, response);
    }
}
