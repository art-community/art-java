package ru.art.http.client.builder;

import static java.lang.String.join;
import static java.util.stream.Collectors.joining;
import static ru.art.core.constants.CharConstants.QUESTION;
import static ru.art.core.constants.StringConstants.*;
import java.util.List;
import java.util.Map;

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
