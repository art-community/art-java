package ru.adk.http.server.constants;

public interface HttpServerLoggingMessages {
    String TOMCAT_STARTED_MESSAGE = "Tomcat HTTP server stated in {0}[ms]";
    String TOMCAT_RESTARTED_MESSAGE = "Tomcat HTTP server restarted in {0}[ms]";
    String REGISTERING_HTTP_INTERCEPTOR = "Registering HTTP interceptor: ''{0}'' for url pattern ''{1}'';";
    String HTTP_SERVICE_REGISTERING_MESSAGE = "Registered HTTP service method for path: ''{0}'' with HTTP service id ''{1}'' and HTTP request types ''{2}''";
    String HTTP_REQUEST_HANDLING_EXCEPTION_MESSAGE = "Exception occurred during request handling:";
    String HTTP_SERVICES_CANCELED = "All http service canceled by interceptors";
    String HTTP_SERVICE_CANCELED = "HTTP service: ''{0}'' canceled by interceptor for path: ''{1}''";
    String HTTP_SERVICE_METHOD_CANCELED = "HTTP service: ''{0}'' method: ''{1}'' canceled by interceptor for path: ''{2}''";
    String HTTP_SERVLET_EVENT = "httpServletHandling";
}
