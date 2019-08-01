package ru.art.grpc.server.interceptor;

import io.grpc.ForwardingServerCallListener.SimpleForwardingServerCallListener;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCall.Listener;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import org.apache.logging.log4j.Logger;
import ru.art.logging.ProtocolCallLoggingParameters;
import static io.grpc.Metadata.ASCII_STRING_MARSHALLER;
import static io.grpc.Metadata.Key.of;
import static java.lang.System.getProperty;
import static java.text.MessageFormat.format;
import static java.util.UUID.randomUUID;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.core.extension.StringExtensions.emptyIfNull;
import static ru.art.grpc.server.constants.GrpcServerLoggingMessages.*;
import static ru.art.grpc.server.constants.GrpcServerModuleConstants.*;
import static ru.art.logging.LoggingModule.loggingModule;
import static ru.art.logging.LoggingParametersManager.clearProtocolLoggingParameters;
import static ru.art.logging.LoggingParametersManager.putProtocolCallLoggingParameters;

public class GrpcServerLoggingInterceptor implements ServerInterceptor {
    private final Logger logger = loggingModule().getLogger(GrpcServerLoggingInterceptor.class);

    @Override
    public <ReqT, RespT> Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall, Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {
        String traceIdHeader = metadata.get(of(TRACE_ID_HEADER, ASCII_STRING_MARSHALLER));
        String profileHeader = metadata.get(of(PROFILE_HEADER, ASCII_STRING_MARSHALLER));
        clearProtocolLoggingParameters();
        putProtocolCallLoggingParameters(ProtocolCallLoggingParameters.builder()
                .profile(profileHeader)
                .environment(getProperty(ENVIRONMENT_PROPERTY))
                .protocol(GRPC_SERVICE_TYPE)
                .requestId(randomUUID().toString())
                .traceId(isEmpty(traceIdHeader) ? randomUUID().toString() : traceIdHeader)
                .build());
        return new SimpleForwardingServerCallListener<ReqT>(serverCallHandler.startCall(serverCall, metadata)) {
            @Override
            public void onMessage(ReqT message) {
                logger.trace(format(GRPC_ON_MESSAGE, emptyIfNull(message)));
                super.onMessage(message);
            }

            @Override
            public void onHalfClose() {
                logger.info(GRPC_ON_HALF_CLOSE);
                super.onHalfClose();
            }

            @Override
            public void onCancel() {
                logger.info(GRPC_ON_CANCEL);
                super.onCancel();
            }

            @Override
            public void onComplete() {
                logger.info(GRPC_ON_COMPLETE);
                super.onComplete();
            }

            @Override
            public void onReady() {
                logger.info(GRPC_ON_READY);
                super.onReady();
            }
        };
    }
}
