package ru.adk.http.constants;

import static ru.adk.core.factory.CollectionsFactory.mapOf;
import java.util.Map;

public enum HttpMethodType {
    GET,
    POST,
    PUT,
    PATCH,
    DELETE,
    HEAD,
    TRACE,
    CONNECT,
    OPTIONS;

    private static final Map<String, HttpMethodType> mappings = mapOf(8);

    static {
        for (HttpMethodType httpMethod : values()) {
            mappings.put(httpMethod.name(), httpMethod);
        }
    }

    public static HttpMethodType resolve(String method) {
        return mappings.get(method);
    }

    public boolean matches(String method) {
        return this == resolve(method);
    }
}
