package ru.adk.grpc.client.communicator;

import io.grpc.ClientInterceptor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.adk.entity.Value;
import ru.adk.entity.mapper.ValueFromModelMapper;
import ru.adk.entity.mapper.ValueToModelMapper;
import ru.adk.grpc.client.exception.GrpcClientException;
import ru.adk.grpc.client.handler.GrpcCommunicationCompletionHandler;
import ru.adk.grpc.client.handler.GrpcCommunicationExceptionHandler;
import static java.util.Collections.emptyList;
import static lombok.AccessLevel.PACKAGE;
import static ru.adk.core.checker.CheckerForEmptiness.isEmpty;
import static ru.adk.grpc.client.constants.GrpcClientExceptionMessages.INVALID_GRPC_COMMUNICATION_CONFIGURATION;
import java.util.List;
import java.util.concurrent.Executor;

@Getter(value = PACKAGE)
@Setter(value = PACKAGE)
@ToString(onlyExplicitlyIncluded = true)
public class GrpcCommunicationConfiguration {
    @ToString.Include
    private String serviceId;
    @ToString.Include
    private String methodId;
    @ToString.Include
    private String url;
    @ToString.Include
    private String path;
    private ValueFromModelMapper<?, ? extends Value> requestMapper;
    private ValueToModelMapper<?, ? extends Value> responseMapper;
    private GrpcCommunicationCompletionHandler<?, ?> completionHandler;
    private GrpcCommunicationExceptionHandler<?> exceptionHandler;
    @Builder.Default
    private List<ClientInterceptor> interceptors = emptyList();
    private Executor overrideExecutor;
    @ToString.Include
    private Object request;
    @ToString.Include
    private long deadlineTimeout;
    @ToString.Include
    private boolean useSecuredTransport;

    void validateRequiredFields() {
        boolean serviceIdIsEmpty = isEmpty(serviceId);
        boolean methodIdIsEmpty = isEmpty(methodId);
        if (serviceIdIsEmpty || methodIdIsEmpty) {
            String message = INVALID_GRPC_COMMUNICATION_CONFIGURATION;
            if (serviceIdIsEmpty) {
                message += "serviceId";
            }
            if (methodIdIsEmpty) {
                message += ",methodId";
            }
            throw new GrpcClientException(message);
        }

    }
}
