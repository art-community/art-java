package ru.art.http.client.constants;

public interface HttpClientExceptionMessages {
    String RESPONSE_CONTENT_TYPE_NOT_SUPPORTED = "Response content mapper ''{0}'' not supported";
    String REQUEST_CONTENT_TYPE_NOT_SUPPORTED = "Request content mapper ''{0}'' not supported";
    String REQUEST_INTERCEPTION_IS_NULL = "Request interception is null";
    String RESPONSE_INTERCEPTION_IS_NULL = "Response interception is null";
    String REQUEST_BODY_READING_EXCEPTION = "Request request reading exception";
    String HTTP_SAL_CONFIGURATION_FAILED = "Failed to configure SSL http client. Please check SSL settings";
    String HTTP_COMMUNICATION_TARGET_NOT_FOUND = "Http communication target for service ''{0}'' was not found";
}
