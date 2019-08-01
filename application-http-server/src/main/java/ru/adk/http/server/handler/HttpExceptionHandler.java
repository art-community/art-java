package ru.adk.http.server.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@FunctionalInterface
public interface HttpExceptionHandler<T extends Exception> {
    void handle(T exception, HttpServletRequest request, HttpServletResponse response);
}
