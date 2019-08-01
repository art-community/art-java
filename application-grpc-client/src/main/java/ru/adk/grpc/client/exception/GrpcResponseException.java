package ru.adk.grpc.client.exception;

import lombok.Getter;
import ru.adk.grpc.client.communicator.GrpcCommunicationConfiguration;
import static java.text.MessageFormat.format;
import static ru.adk.grpc.client.constants.GrpcClientExceptionMessages.GRPC_CLIENT_EXCEPTION_MESSAGE;

@Getter
public class GrpcResponseException extends RuntimeException {
    private final String errorCode;
    private final String errorMessage;

    public GrpcResponseException(GrpcCommunicationConfiguration configuration, String errorCode, String message) {
        super(format(GRPC_CLIENT_EXCEPTION_MESSAGE, configuration, errorCode, message));
        this.errorCode = errorCode;
        this.errorMessage = message;
    }
}
