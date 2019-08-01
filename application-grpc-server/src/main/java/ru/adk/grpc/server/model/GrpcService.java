package ru.adk.grpc.server.model;

import lombok.*;
import lombok.experimental.Accessors;
import ru.adk.entity.mapper.ValueFromModelMapper;
import ru.adk.entity.mapper.ValueToModelMapper;
import ru.adk.service.constants.RequestValidationPolicy;
import java.util.Map;

@Getter
@Builder(builderMethodName = "grpcService", buildMethodName = "serve")
public class GrpcService {
    @Singular("method")
    private Map<String, GrpcMethod> methods;

    @Getter
    @Setter
    @Accessors(fluent = true)
    @NoArgsConstructor(staticName = "grpcMethod")
    public static class GrpcMethod {
        private ValueToModelMapper requestMapper;
        private ValueFromModelMapper responseMapper;
        private RequestValidationPolicy validationPolicy;
    }
}
