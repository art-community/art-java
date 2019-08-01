package ru.adk.http.server.constants;

public interface HttpExceptionResponses {
    String SERVICE_EXCEPTION_HANDLING_ERROR_RESPONSE = "'{'\"errorCode\": \"{0}\",\"errorMessage\": \"{1}\"'}'";
    String EXCEPTION_HANDLING_ERROR_RESPONSE = "'{'\"errorCode\": \"HTTP_SERVER_EXCEPTION\",\"errorMessage\": \"{0}\"'}'";
}
