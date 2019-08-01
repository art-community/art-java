package ru.adk.grpc.server.function;

import ru.adk.entity.mapper.ValueFromModelMapper;
import ru.adk.entity.mapper.ValueToModelMapper;
import ru.adk.grpc.server.model.GrpcService;
import ru.adk.grpc.server.model.GrpcService.GrpcMethod;
import ru.adk.service.constants.RequestValidationPolicy;
import static ru.adk.core.caster.Caster.cast;
import static ru.adk.grpc.server.constants.GrpcServerModuleConstants.EXECUTE_GRPC_FUNCTION;
import static ru.adk.service.ServiceModule.serviceModule;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class GrpcServiceFunction {
    private GrpcMethod grpcMethod = GrpcMethod.grpcMethod();
    private final String serviceId;

    private GrpcServiceFunction(String serviceId) {
        this.serviceId = serviceId;
    }

    public GrpcServiceFunction responseMapper(ValueFromModelMapper responseMapper) {
        grpcMethod.responseMapper(responseMapper);
        return this;
    }

    public GrpcServiceFunction requestMapper(ValueToModelMapper requestMapper) {
        grpcMethod.requestMapper(requestMapper);
        return this;
    }

    public GrpcServiceFunction validationPolicy(RequestValidationPolicy policy) {
        grpcMethod.validationPolicy(policy);
        return this;
    }

    public void handle(Function<?, ?> function) {
        serviceModule()
                .getServiceRegistry()
                .registerService(new GrpcFunctionalServiceSpecification(serviceId, GrpcService.grpcService().method(EXECUTE_GRPC_FUNCTION, grpcMethod).serve(), function));
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

    public static GrpcServiceFunction grpc(String serviceId) {
        return new GrpcServiceFunction(serviceId);
    }
}