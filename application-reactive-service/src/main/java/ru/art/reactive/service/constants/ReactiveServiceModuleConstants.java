package ru.art.reactive.service.constants;

public interface ReactiveServiceModuleConstants {
    String REACTIVE_SERVICE_MODULE_ID = "REACTIVE_SERVICE_MODULE";
    String REACTIVE_SERVICE_TYPE = "REACTIVE_SERVICE";

    enum ReactiveMethodProcessingMode {
        STRAIGHT,
        REACTIVE
    }
}
