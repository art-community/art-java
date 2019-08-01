package ru.art.http.server.builder;

import ru.art.http.server.path.HttpPath;

public interface HttpPathBuilder {
    static HttpPath buildHttpPath(String serviceContextPath, HttpPath methodPath) {
        return HttpPath.builder()
                .contextPath(serviceContextPath + methodPath.getContextPath())
                .pathParameters(methodPath.getPathParameters())
                .build();
    }
}
