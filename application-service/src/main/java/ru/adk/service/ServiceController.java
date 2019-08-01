package ru.adk.service;

import ru.adk.service.exception.ServiceExecutionException;
import ru.adk.service.interceptor.ServiceExecutionInterceptor.ServiceRequestInterceptor;
import ru.adk.service.model.*;
import static java.text.MessageFormat.format;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static ru.adk.core.caster.Caster.cast;
import static ru.adk.core.constants.DateConstants.YYYY_MM_DD_HH_MM_SS_24H_Z_DOT_FORMAT;
import static ru.adk.core.constants.InterceptionStrategy.PROCESS_HANDLING;
import static ru.adk.core.constants.InterceptionStrategy.STOP_HANDLING;
import static ru.adk.logging.LoggingModuleConstants.LoggingParameters.*;
import static ru.adk.logging.ThreadContextExtensions.putIfNotNull;
import static ru.adk.service.ServiceModule.serviceModule;
import static ru.adk.service.ServiceResponseDataExtractor.extractResponseDataChecked;
import static ru.adk.service.constants.ServiceErrorCodes.INTERNAL_ERROR;
import static ru.adk.service.constants.ServiceExceptionsMessages.SERVICE_WITH_ID_NOT_EXISTS;
import static ru.adk.service.factory.ServiceRequestFactory.newServiceRequest;
import static ru.adk.service.factory.ServiceResponseFactory.okResponse;
import static ru.adk.service.interceptor.ServiceExecutionInterceptor.ServiceResponseInterceptor;
import static ru.adk.service.model.ServiceInterceptionResult.nextInterceptor;
import static ru.adk.service.model.ServiceInterceptionResult.stopHandling;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ServiceController {
    static <RequestType, ResponseType> Optional<ResponseType> executeServiceMethod(String serviceId, String methodId, RequestType requestData) {
        ServiceMethodCommand command = new ServiceMethodCommand(serviceId, methodId);
        ServiceRequest<RequestType> request = newServiceRequest(command, requestData);
        return ofNullable(extractResponseDataChecked(executeServiceMethodUnchecked(request)));
    }

    static <ResponseType> Optional<ResponseType> executeServiceMethod(String serviceId, String methodId) {
        ServiceMethodCommand command = new ServiceMethodCommand(serviceId, methodId);
        ServiceRequest<?> request = newServiceRequest(command);
        return ofNullable(extractResponseDataChecked(executeServiceMethodUnchecked(request)));
    }

    static <RequestType, ResponseType> ServiceResponse<ResponseType> executeServiceMethodUnchecked(String serviceId, String methodId, RequestType requestData) {
        ServiceMethodCommand command = new ServiceMethodCommand(serviceId, methodId);
        ServiceRequest<RequestType> request = newServiceRequest(command, requestData);
        return executeServiceMethodUnchecked(request);
    }

    static <ResponseType> ServiceResponse<ResponseType> executeServiceMethodUnchecked(String serviceId, String methodId) {
        ServiceMethodCommand command = new ServiceMethodCommand(serviceId, methodId);
        ServiceRequest<?> request = newServiceRequest(command);
        return executeServiceMethodUnchecked(request);
    }

    static <RequestType, ResponseType> ServiceResponse<ResponseType> executeServiceMethodUnchecked(ServiceRequest<RequestType> request) {
        Date startTime = new Date();
        putIfNotNull(REQUEST_START_TIME_KEY, YYYY_MM_DD_HH_MM_SS_24H_Z_DOT_FORMAT.format(startTime));
        Specification service = serviceModule()
                .getServiceRegistry()
                .getService(request.getServiceMethodCommand().getServiceId());
        if (isNull(service)) return ServiceResponse.<ResponseType>builder()
                .serviceException(new ServiceExecutionException(request.getServiceMethodCommand(), INTERNAL_ERROR, format(SERVICE_WITH_ID_NOT_EXISTS, request.getServiceMethodCommand().getServiceId())))
                .build();
        ServiceInterceptionResult serviceInterceptionResult;
        if (nonNull((serviceInterceptionResult = beforeServiceExecution(service, request)).getResponse()))
            return cast(serviceInterceptionResult.getResponse());
        ServiceResponse<ResponseType> response = service.getExceptionWrapper().executeServiceWrapped(request.getServiceMethodCommand(), serviceInterceptionResult.getRequest());
        Date endTime = new Date();
        putIfNotNull(EXECUTION_TIME_KEY, endTime.getTime() - startTime.getTime());
        putIfNotNull(REQUEST_END_TIME_KEY, YYYY_MM_DD_HH_MM_SS_24H_Z_DOT_FORMAT.format(endTime));
        return cast(afterServiceExecution(request, service, response).getResponse());
    }

    static <RequestType> ServiceInterceptionResult beforeServiceExecution(Specification service, ServiceRequest<RequestType> request) {
        ServiceDeactivationConfig deactivationConfig = service.getDeactivationConfig();
        if (deactivationConfig.isDeactivated() || deactivationConfig.getDeactivatedMethods().contains(request.getServiceMethodCommand().getMethodId()))
            return stopHandling(request, okResponse(request.getServiceMethodCommand()));
        List<ServiceRequestInterceptor> methodRequestInterceptors = service.getMethodRequestInterceptors().get(request.getServiceMethodCommand().getMethodId());
        List<ServiceRequestInterceptor> requestInterceptors = service.getRequestInterceptors();
        ServiceInterceptionResult serviceInterceptionResult = nextInterceptor(request);
        for (ServiceRequestInterceptor interceptor : requestInterceptors) {
            serviceInterceptionResult = cast(interceptor.getInterception().intercept(serviceInterceptionResult.getRequest()));
            if (serviceInterceptionResult.getNextInterceptionStrategy() == PROCESS_HANDLING) break;
            if (serviceInterceptionResult.getNextInterceptionStrategy() == STOP_HANDLING) {
                return serviceInterceptionResult;
            }
        }

        if (isNull(methodRequestInterceptors)) {
            return serviceInterceptionResult;
        }

        for (ServiceRequestInterceptor interceptor : methodRequestInterceptors) {
            serviceInterceptionResult = cast(interceptor.getInterception().intercept(serviceInterceptionResult.getRequest()));
            if (serviceInterceptionResult.getNextInterceptionStrategy() == PROCESS_HANDLING) break;
            if (serviceInterceptionResult.getNextInterceptionStrategy() == STOP_HANDLING) {
                return serviceInterceptionResult;
            }
        }

        return serviceInterceptionResult;
    }

    static <RequestType, ResponseType> ServiceInterceptionResult afterServiceExecution(ServiceRequest<RequestType> request, Specification service, ServiceResponse<ResponseType> response) {
        List<ServiceResponseInterceptor> methodResponseInterceptors = service.getMethodResponseInterceptors().get(request.getServiceMethodCommand().getMethodId());
        List<ServiceResponseInterceptor> responseInterceptors = service.getResponseInterceptors();
        ServiceInterceptionResult serviceInterceptionResult = nextInterceptor(request, response);
        for (ServiceResponseInterceptor interceptor : responseInterceptors) {
            serviceInterceptionResult = cast(interceptor.getInterception().intercept(serviceInterceptionResult.getRequest(), response));
            if (serviceInterceptionResult.getNextInterceptionStrategy() == PROCESS_HANDLING) break;
            if (serviceInterceptionResult.getNextInterceptionStrategy() == STOP_HANDLING) {
                return serviceInterceptionResult;
            }
        }

        if (isNull(methodResponseInterceptors)) {
            return serviceInterceptionResult;
        }

        for (ServiceResponseInterceptor interceptor : methodResponseInterceptors) {
            serviceInterceptionResult = cast(interceptor.getInterception().intercept(serviceInterceptionResult.getRequest(), response));
            if (serviceInterceptionResult.getNextInterceptionStrategy() == PROCESS_HANDLING) break;
            if (serviceInterceptionResult.getNextInterceptionStrategy() == STOP_HANDLING) {
                return serviceInterceptionResult;
            }
        }

        return serviceInterceptionResult;
    }
}
