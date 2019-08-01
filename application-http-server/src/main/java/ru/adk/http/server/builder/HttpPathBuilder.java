package ru.adk.http.server.builder;

import ru.adk.http.server.path.HttpPath;

public interface HttpPathBuilder {
    static HttpPath buildHttpPath(String serviceContextPath, HttpPath methodPath) {
        return HttpPath.builder()
                .contextPath(serviceContextPath + methodPath.getContextPath())
                .pathParameters(methodPath.getPathParameters())
                .build();
    }
}
