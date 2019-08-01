package ru.adk.rsocket.function;

import ru.adk.entity.mapper.ValueFromModelMapper;
import ru.adk.entity.mapper.ValueToModelMapper;
import ru.adk.reactive.service.constants.ReactiveServiceModuleConstants.ReactiveMethodProcessingMode;
import ru.adk.reactive.service.model.ReactiveService;
import ru.adk.rsocket.constants.RsocketModuleConstants.RsocketDataFormat;
import ru.adk.rsocket.service.RsocketService;
import ru.adk.rsocket.service.RsocketService.RsocketMethod;
import ru.adk.service.constants.RequestValidationPolicy;
import static java.util.UUID.randomUUID;
import static ru.adk.core.caster.Caster.cast;
import static ru.adk.core.constants.CharConstants.UNDERSCORE;
import static ru.adk.reactive.service.model.ReactiveService.ReactiveMethod;
import static ru.adk.rsocket.constants.RsocketModuleConstants.EXECUTE_RSOCKET_FUNCTION;
import static ru.adk.rsocket.constants.RsocketModuleConstants.RSOCKET_SERVICE_TYPE;
import static ru.adk.service.ServiceModule.serviceModule;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class RsocketServiceFunction {
    private RsocketMethod rsocketMethod = RsocketMethod.rsocketMethod();
    private ReactiveMethod reactiveMethod = ReactiveMethod.reactiveMethod();
    private final String serviceId;

    private RsocketServiceFunction(String serviceId) {
        this.serviceId = serviceId;
    }

    public RsocketServiceFunction responseMapper(ValueFromModelMapper responseMapper) {
        rsocketMethod.responseMapper(responseMapper);
        return this;
    }

    public RsocketServiceFunction overrideResponseDataFormat(RsocketDataFormat dataFormat) {
        rsocketMethod.overrideResponseDataFormat(dataFormat);
        return this;
    }

    public RsocketServiceFunction requestMapper(ValueToModelMapper requestMapper) {
        rsocketMethod.requestMapper(requestMapper);
        return this;
    }

    public RsocketServiceFunction requestProcessingMode(ReactiveMethodProcessingMode methodProcessingMode) {
        reactiveMethod.requestProcessingMode(methodProcessingMode);
        return this;
    }

    public RsocketServiceFunction responseProcessingMode(ReactiveMethodProcessingMode methodProcessingMode) {
        reactiveMethod.responseProcessingMode(methodProcessingMode);
        return this;
    }

    public RsocketServiceFunction validationPolicy(RequestValidationPolicy policy) {
        rsocketMethod.validationPolicy(policy);
        return this;
    }

    public void handle(Function<?, ?> function) {
        serviceModule()
                .getServiceRegistry()
                .registerService(new RsocketFunctionalServiceSpecification(serviceId,
                        RsocketService.rsocketService()
                                .method(EXECUTE_RSOCKET_FUNCTION, rsocketMethod)
                                .serve(),
                        ReactiveService.reactiveService()
                                .method(EXECUTE_RSOCKET_FUNCTION, reactiveMethod)
                                .serve(),
                        function));
    }

    public void consume(Consumer<?> consumer) {
        handle(request -> {
            consumer.accept(cast(request));
            return null;
        });
    }

    public void produce(Supplier<?> producer) {
        handle(request -> producer.get());
    }

    public static RsocketServiceFunction rsocket(String serviceId) {
        return new RsocketServiceFunction(serviceId);
    }
}