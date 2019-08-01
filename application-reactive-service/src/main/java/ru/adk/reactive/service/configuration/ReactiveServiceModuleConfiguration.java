package ru.adk.reactive.service.configuration;

import lombok.Getter;
import ru.adk.core.module.ModuleConfiguration;
import ru.adk.reactive.service.interception.ReactiveServiceLoggingInterception;
import ru.adk.reactive.service.interception.ReactiveServiceValidationInterception;
import static ru.adk.core.factory.CollectionsFactory.linkedListOf;
import static ru.adk.service.interceptor.ServiceExecutionInterceptor.*;
import java.util.List;

public interface ReactiveServiceModuleConfiguration extends ModuleConfiguration {
    List<ServiceRequestInterceptor> getRequestInterceptors();

    List<ServiceResponseInterceptor> getResponseInterceptors();

    class ReactiveServiceModuleDefaultConfiguration implements ReactiveServiceModuleConfiguration {
        @Getter(lazy = true)
        private final List<ServiceRequestInterceptor> requestInterceptors = linkedListOf(interceptRequest(new ReactiveServiceLoggingInterception()), interceptRequest(new ReactiveServiceValidationInterception()));
        @Getter(lazy = true)
        private final List<ServiceResponseInterceptor> responseInterceptors = linkedListOf(interceptResponse(new ReactiveServiceLoggingInterception()));
    }
}
