package ru.art.grpc.client.interceptor;

import io.grpc.*;
import static io.grpc.Metadata.ASCII_STRING_MARSHALLER;
import static io.grpc.Metadata.Key.of;
import static org.apache.logging.log4j.ThreadContext.get;
import static ru.art.core.checker.CheckerForEmptiness.isNotEmpty;
import static ru.art.grpc.client.constants.GrpcClientModuleConstants.TRACE_ID_HEADER;
import static ru.art.logging.LoggingModuleConstants.LoggingParameters.TRACE_ID_KEY;

public class GrpcClientTracingInterceptor implements ClientInterceptor {
    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> methodDescriptor, CallOptions callOptions, Channel channel) {
        return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(channel.newCall(methodDescriptor, callOptions)) {
            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                String traceId = get(TRACE_ID_KEY);
                if (isNotEmpty(traceId)) {
                    headers.put(of(TRACE_ID_HEADER, ASCII_STRING_MARSHALLER), traceId);
                }
                super.start(responseListener, headers);
            }
        };
    }
}
