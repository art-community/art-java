package io.art.service.caller;

import lombok.*;
import io.art.service.constants.*;
import io.art.service.model.*;
import static java.util.Optional.*;
import static lombok.AccessLevel.*;
import static io.art.service.ServiceController.*;
import static io.art.service.ServiceResponseDataExtractor.*;
import static io.art.service.constants.RequestValidationPolicy.*;
import static io.art.service.factory.ServiceRequestFactory.*;
import java.util.*;

@RequiredArgsConstructor(access = PRIVATE)
public class ServiceMethodCaller {
    private final String serviceId;
    private String methodId;
    private RequestValidationPolicy validationPolicy = NON_VALIDATABLE;

    public static ServiceMethodCaller service(String serviceId) {
        return new ServiceMethodCaller(serviceId);
    }

    public ServiceMethodCaller method(String methodId) {
        this.methodId = methodId;
        return this;
    }

    public ServiceMethodCaller validatable() {
        validationPolicy = VALIDATABLE;
        return this;
    }

    public ServiceMethodCaller notNull() {
        validationPolicy = NOT_NULL;
        return this;
    }

    public <ResponseType> ServiceResponse<ResponseType> execute() {
        return executeServiceMethodUnchecked(newServiceRequest(new ServiceMethodCommand(serviceId, methodId), validationPolicy));
    }

    public <RequestType, ResponseType> ServiceResponse<ResponseType> execute(RequestType request) {
        return executeServiceMethodUnchecked(newServiceRequest(new ServiceMethodCommand(serviceId, methodId), request, validationPolicy));
    }

    public <ResponseType> Optional<ResponseType> call() {
        return ofNullable(extractResponseDataChecked(executeServiceMethodUnchecked(newServiceRequest(new ServiceMethodCommand(serviceId, methodId), validationPolicy))));
    }

    public <RequestType, ResponseType> Optional<ResponseType> call(RequestType request) {
        return ofNullable(extractResponseDataChecked(executeServiceMethodUnchecked(newServiceRequest(new ServiceMethodCommand(serviceId, methodId), request, validationPolicy))));
    }
}
