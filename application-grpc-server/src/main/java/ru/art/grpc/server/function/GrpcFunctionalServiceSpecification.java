package ru.art.grpc.server.function;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.art.grpc.server.model.GrpcService;
import ru.art.grpc.server.specification.GrpcServiceSpecification;
import static ru.art.core.caster.Caster.cast;
import java.util.function.Function;

@Getter
@RequiredArgsConstructor
public class GrpcFunctionalServiceSpecification implements GrpcServiceSpecification {
    private final String serviceId;
    private final GrpcService grpcService;
    private final Function<?, ?> function;

    @Override
    public <P, R> R executeMethod(String methodId, P request) {
        return cast(function.apply(cast(request)));
    }
}