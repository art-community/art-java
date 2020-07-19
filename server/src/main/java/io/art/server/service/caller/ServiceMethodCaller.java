package io.art.server.service.caller;

import io.art.server.constants.*;
import lombok.*;
import static io.art.server.constants.RequestValidationPolicy.*;
import static lombok.AccessLevel.*;
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

    public <ResponseType> ResponseType execute() {
    }

    public <RequestType, ResponseType> ResponseType execute(RequestType request) {

    }

    public <ResponseType> Optional<ResponseType> call() {
    }

    public <RequestType, ResponseType> Optional<ResponseType> call(RequestType request) {
    }
}
