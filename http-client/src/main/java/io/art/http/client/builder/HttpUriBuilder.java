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

package io.art.http.client.builder;

import static java.lang.String.*;
import static java.util.stream.Collectors.*;
import static io.art.core.constants.CharConstants.*;
import static io.art.core.constants.StringConstants.EQUAL;
import static io.art.core.constants.StringConstants.SLASH;
import static io.art.core.constants.StringConstants.*;
import java.util.*;

public interface HttpUriBuilder {
    static String buildUri(String baseUri, List<String> pathParameters) {
        return baseUri + join(SLASH, pathParameters);
    }

    static String buildUri(String baseUri, Map<String, String> queryParameters) {
        return baseUri + queryParameters
                .entrySet()
                .stream().map(e -> e.getKey() + EQUAL + e.getValue())
                .collect(joining(AMPERSAND));
    }

    static String buildUri(String baseUri, List<String> pathParameters, Map<String, String> queryParameters) {
        String baseUrl = baseUri + join(SLASH, pathParameters);
        if (queryParameters.isEmpty()) return baseUrl;
        return baseUrl + QUESTION + queryParameters
                .entrySet()
                .stream()
                .map(e -> e.getKey() + EQUAL + e.getValue())
                .collect(joining(AMPERSAND));
    }
}
