package ru.adk.network.manager.interceptor;

import io.grpc.*;
import static ru.adk.network.manager.client.ApplicationStateClient.decrementSession;
import static ru.adk.network.manager.client.ApplicationStateClient.incrementSession;

public class ProtobufServerSessionInterceptor implements ServerInterceptor {
    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall, Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {
        return new ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT>(serverCallHandler.startCall(serverCall, metadata)) {
            @Override
            public void onComplete() {
                decrementSession();
                super.onComplete();
            }

            @Override
            public void onReady() {
                incrementSession();
                super.onReady();
            }
        };
    }
}
