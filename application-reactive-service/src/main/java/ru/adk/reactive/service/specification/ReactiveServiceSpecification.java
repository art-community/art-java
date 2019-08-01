package ru.adk.reactive.service.specification;

import ru.adk.reactive.service.model.ReactiveService;
import ru.adk.service.Specification;
import static ru.adk.core.factory.CollectionsFactory.fixedArrayOf;
import static ru.adk.reactive.service.constants.ReactiveServiceModuleConstants.REACTIVE_SERVICE_TYPE;
import static ru.adk.reactive.service.module.ReactiveServiceModule.reactiveServiceModule;
import static ru.adk.service.interceptor.ServiceExecutionInterceptor.ServiceRequestInterceptor;
import static ru.adk.service.interceptor.ServiceExecutionInterceptor.ServiceResponseInterceptor;
import java.util.List;

public interface ReactiveServiceSpecification extends Specification {
    ReactiveService getReactiveService();

    @Override
    default List<ServiceRequestInterceptor> getRequestInterceptors() {
        return reactiveServiceModule().getRequestInterceptors();
    }

    @Override
    default List<ServiceResponseInterceptor> getResponseInterceptors() {
        return reactiveServiceModule().getResponseInterceptors();
    }

    @Override
    default List<String> getServiceTypes() {
        return fixedArrayOf(REACTIVE_SERVICE_TYPE);
    }
}
