package ru.adk.service;

import lombok.Getter;
import org.apache.logging.log4j.Logger;
import ru.adk.logging.ServiceCallLoggingParameters;
import ru.adk.service.exception.ServiceExecutionException;
import ru.adk.service.interceptor.ServiceRequestInterception;
import ru.adk.service.interceptor.ServiceResponseInterception;
import ru.adk.service.model.ServiceInterceptionResult;
import ru.adk.service.model.ServiceRequest;
import ru.adk.service.model.ServiceResponse;
import static java.text.MessageFormat.format;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.logging.log4j.ThreadContext.get;
import static org.apache.logging.log4j.ThreadContext.remove;
import static ru.adk.core.checker.CheckerForEmptiness.isEmpty;
import static ru.adk.core.constants.StringConstants.DOT;
import static ru.adk.core.extension.NullCheckingExtensions.getOrElse;
import static ru.adk.core.factory.CollectionsFactory.stackOf;
import static ru.adk.logging.LoggingModule.loggingModule;
import static ru.adk.logging.LoggingModuleConstants.DEFAULT_REQUEST_ID;
import static ru.adk.logging.LoggingModuleConstants.LoggingParameters.*;
import static ru.adk.logging.LoggingParametersManager.putServiceCallLoggingParameters;
import static ru.adk.logging.ThreadContextExtensions.putIfNotNull;
import static ru.adk.service.ServiceModule.serviceModule;
import static ru.adk.service.constants.ServiceLoggingMessages.*;
import static ru.adk.service.constants.ServiceModuleConstants.REQUEST_EVENT;
import static ru.adk.service.constants.ServiceModuleConstants.RESPONSE_EVENT;
import static ru.adk.service.model.ServiceInterceptionResult.nextInterceptor;
import java.util.List;
import java.util.Stack;

public class ServiceLoggingInterception implements ServiceRequestInterception, ServiceResponseInterception {
    private final static ThreadLocal<Stack<ServiceCallLoggingParameters>> serviceLoggingParameters = new ThreadLocal<>();
    @Getter(lazy = true)
    private final Logger logger = loggingModule().getLogger(ServiceLoggingInterception.class);

    private static void putRequestResponseMetrics(ServiceRequest<?> request, ServiceCallLoggingParameters parameters) {
        putIfNotNull(REQUEST_KEY, request);
        putServiceCallLoggingParameters(parameters);
        List<String> serviceTypes = serviceModule()
                .getServiceRegistry()
                .getService(request.getServiceMethodCommand().getServiceId())
                .getServiceTypes();
        putIfNotNull(SERVICE_TYPES_KEY, serviceTypes);
    }

    @Override
    public ServiceInterceptionResult intercept(ServiceRequest<?> request) {
        if (isNull(serviceLoggingParameters.get())) {
            serviceLoggingParameters.set(stackOf());
        }
        ServiceCallLoggingParameters parameters = ServiceCallLoggingParameters.builder()
                .serviceId(request.getServiceMethodCommand().getServiceId())
                .serviceMethodId(request.getServiceMethodCommand().toString())
                .serviceMethodCommand(request.getServiceMethodCommand().toString() + DOT + getOrElse(get(REQUEST_ID_KEY), DEFAULT_REQUEST_ID))
                .build();
        serviceLoggingParameters.get().push(parameters);
        putRequestResponseMetrics(request, parameters);
        putIfNotNull(SERVICE_EVENT_TYPE_KEY, REQUEST_EVENT);
        getLogger().info(format(EXECUTION_SERVICE_MESSAGE, request.getServiceMethodCommand(), getOrElse(get(REQUEST_ID_KEY), DEFAULT_REQUEST_ID), request));
        remove(SERVICE_EVENT_TYPE_KEY);
        return nextInterceptor(request);
    }

    @Override
    public ServiceInterceptionResult intercept(ServiceRequest<?> request, ServiceResponse<?> response) {
        if (!isEmpty(serviceLoggingParameters.get())) {
            putRequestResponseMetrics(request, serviceLoggingParameters.get().pop());
        }
        ServiceExecutionException serviceException = response.getServiceException();
        if (nonNull(serviceException)) {
            putIfNotNull(RESPONSE_KEY, response);
            putIfNotNull(SERVICE_EXCEPTION_KEY, serviceException);
            putIfNotNull(SERVICE_EVENT_TYPE_KEY, RESPONSE_EVENT);
            getLogger().error(format(SERVICE_FAILED_MESSAGE, request.getServiceMethodCommand(), getOrElse(get(REQUEST_ID_KEY), DEFAULT_REQUEST_ID), serviceException.getErrorCode(), serviceException.getErrorMessage()), serviceException);
            return nextInterceptor(request, response);
        }
        putIfNotNull(RESPONSE_KEY, response);
        putIfNotNull(SERVICE_EVENT_TYPE_KEY, RESPONSE_EVENT);
        getLogger().info(format(SERVICE_EXECUTED_MESSAGE, request.getServiceMethodCommand(), getOrElse(get(REQUEST_ID_KEY), DEFAULT_REQUEST_ID), response));
        return nextInterceptor(request, response);
    }
}
