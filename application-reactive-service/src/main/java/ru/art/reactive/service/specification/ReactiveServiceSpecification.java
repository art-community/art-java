package ru.art.reactive.service.specification;

import ru.art.reactive.service.model.ReactiveService;
import ru.art.service.Specification;
import static ru.art.core.factory.CollectionsFactory.fixedArrayOf;
import static ru.art.reactive.service.constants.ReactiveServiceModuleConstants.REACTIVE_SERVICE_TYPE;
import static ru.art.reactive.service.module.ReactiveServiceModule.reactiveServiceModule;
import static ru.art.service.interceptor.ServiceExecutionInterceptor.ServiceRequestInterceptor;
import static ru.art.service.interceptor.ServiceExecutionInterceptor.ServiceResponseInterceptor;
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
