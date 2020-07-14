/*
 * ART Java
 *
 * Copyright 2019 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.art.grpc.servlet;

import com.google.common.util.concurrent.*;
import com.google.protobuf.*;
import io.grpc.*;
import io.grpc.protobuf.*;
import io.grpc.stub.*;
import io.grpc.stub.ServerCalls.*;
import lombok.*;
import ru.art.grpc.exception.*;
import static com.google.protobuf.StructProto.*;
import static io.grpc.MethodDescriptor.*;
import static io.grpc.protobuf.ProtoUtils.*;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.*;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.*;
import static java.text.MessageFormat.*;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.grpc.constants.GrpcConstants.*;
import static ru.art.grpc.constants.GrpcExceptionMessages.*;

public class GrpcServlet {
    private volatile MethodDescriptor<com.google.protobuf.Value, com.google.protobuf.Value> methodDescriptor;
    private volatile ServiceDescriptor serviceDescriptor;

    public MethodDescriptor<com.google.protobuf.Value, com.google.protobuf.Value> getExecuteServiceMethod(String path) {
        return getExecuteServiceMethodHelper(path);
    }

    private String extractServletName(String path) {
        try {
            return path.substring(path.lastIndexOf(DOT));
        } catch (Throwable throwable) {
            throw new GrpcException(format(UNABLE_TO_EXTRACT_SERVLET_NAME, path));
        }
    }

    private MethodDescriptor<com.google.protobuf.Value, com.google.protobuf.Value> getExecuteServiceMethodHelper(String path) {
        MethodDescriptor<com.google.protobuf.Value, com.google.protobuf.Value> methodDescriptorLocal;
        if ((methodDescriptorLocal = methodDescriptor) != null) {
            return methodDescriptorLocal;
        }
        synchronized (GrpcServlet.class) {
            if ((methodDescriptorLocal = methodDescriptor) != null) {
                return methodDescriptorLocal;
            }
            methodDescriptor = methodDescriptorLocal = MethodDescriptor.<com.google.protobuf.Value, com.google.protobuf.Value>newBuilder()
                    .setType(MethodDescriptor.MethodType.UNARY)
                    .setFullMethodName(generateFullMethodName(path, METHOD_NAME))
                    .setSampledToLocalTracing(true)
                    .setRequestMarshaller(marshaller(com.google.protobuf.Value.getDefaultInstance()))
                    .setResponseMarshaller(marshaller(com.google.protobuf.Value.getDefaultInstance()))
                    .setSchemaDescriptor(new GrpcServletMethodDescriptorSupplier(path))
                    .build();
        }
        return methodDescriptorLocal;
    }

    public GrpcServletStub newStub(Channel channel, String path) {
        return new GrpcServletStub(channel, path);
    }

    public GrpcServletBlockingStub newBlockingStub(Channel channel, String path) {
        return new GrpcServletBlockingStub(channel, path);
    }

    public GrpcServletFutureStub newFutureStub(Channel channel, String path) {
        return new GrpcServletFutureStub(channel, path);
    }

    private ServiceDescriptor getServiceDescriptor(String path) {
        ServiceDescriptor result = serviceDescriptor;
        if (result != null) {
            return result;
        }
        synchronized (GrpcServlet.class) {
            result = serviceDescriptor;
            if (result != null) {
                return result;
            }
            serviceDescriptor = result = ServiceDescriptor.newBuilder(path)
                    .setSchemaDescriptor(new GrpcServletFileDescriptorSupplier(path))
                    .addMethod(getExecuteServiceMethodHelper(path))
                    .build();
        }
        return result;
    }

    @AllArgsConstructor
    public abstract class GrpcServletImplBase implements BindableService {
        private String path;

        public void executeService(com.google.protobuf.Value request, StreamObserver<com.google.protobuf.Value> responseObserver) {
            asyncUnimplementedUnaryCall(getExecuteServiceMethodHelper(path), responseObserver);
        }

        @Override
        public final ServerServiceDefinition bindService() {
            return ServerServiceDefinition.builder(getServiceDescriptor(path))
                    .addMethod(getExecuteServiceMethodHelper(path), asyncUnaryCall(new MethodHandlers<>(this)))
                    .build();
        }
    }

    public final class GrpcServletStub extends AbstractStub<GrpcServletStub> {
        private final String path;

        private GrpcServletStub(Channel channel, String path) {
            super(channel);
            this.path = path;
        }

        private GrpcServletStub(Channel channel, CallOptions callOptions, String path) {
            super(channel, callOptions);
            this.path = path;
        }

        @Override
        protected GrpcServletStub build(Channel channel, CallOptions callOptions) {
            return new GrpcServletStub(channel, callOptions, path);
        }

        public void executeService(com.google.protobuf.Value request, StreamObserver<com.google.protobuf.Value> responseObserver) {
            asyncUnaryCall(getChannel().newCall(getExecuteServiceMethodHelper(path), getCallOptions()), request, responseObserver);
        }
    }

    public final class GrpcServletBlockingStub extends AbstractStub<GrpcServletBlockingStub> {
        private final String path;

        private GrpcServletBlockingStub(Channel channel, String path) {
            super(channel);
            this.path = path;
        }

        private GrpcServletBlockingStub(Channel channel, CallOptions callOptions, String path) {
            super(channel, callOptions);
            this.path = path;
        }

        @Override
        protected GrpcServletBlockingStub build(Channel channel, CallOptions callOptions) {
            return new GrpcServletBlockingStub(channel, callOptions, path);
        }

        public com.google.protobuf.Value executeService(com.google.protobuf.Value request) {
            return blockingUnaryCall(getChannel(), getExecuteServiceMethodHelper(path), getCallOptions(), request);
        }
    }

    public final class GrpcServletFutureStub extends AbstractStub<GrpcServletFutureStub> {
        private final String path;

        private GrpcServletFutureStub(Channel channel, String path) {
            super(channel);
            this.path = path;
        }

        private GrpcServletFutureStub(Channel channel, CallOptions callOptions, String path) {
            super(channel, callOptions);
            this.path = path;
        }

        @Override
        protected GrpcServletFutureStub build(Channel channel, CallOptions callOptions) {
            return new GrpcServletFutureStub(channel, callOptions, path);
        }

        public ListenableFuture<com.google.protobuf.Value> executeService(com.google.protobuf.Value request) {
            return futureUnaryCall(getChannel().newCall(getExecuteServiceMethodHelper(path), getCallOptions()), request);
        }
    }

    private final class MethodHandlers<RequestType, ResponseType> implements UnaryMethod<RequestType, ResponseType>, ServerStreamingMethod<RequestType, ResponseType>, ClientStreamingMethod<RequestType, ResponseType>, BidiStreamingMethod<RequestType, ResponseType> {
        private final GrpcServletImplBase serviceImpl;

        MethodHandlers(GrpcServletImplBase serviceImpl) {
            this.serviceImpl = serviceImpl;
        }

        @Override
        @java.lang.SuppressWarnings("unchecked")
        public void invoke(RequestType request, StreamObserver<ResponseType> responseObserver) {
            serviceImpl.executeService((com.google.protobuf.Value) request, (StreamObserver<com.google.protobuf.Value>) responseObserver);
        }

        @Override
        public StreamObserver<RequestType> invoke(StreamObserver<ResponseType> responseObserver) {
            throw new AssertionError();
        }
    }

    @AllArgsConstructor
    private abstract class GrpcServletBaseDescriptorSupplier implements ProtoFileDescriptorSupplier, ProtoServiceDescriptorSupplier {
        private final String path;

        @Override
        public Descriptors.FileDescriptor getFileDescriptor() {
            return getDescriptor();
        }

        @Override
        public Descriptors.ServiceDescriptor getServiceDescriptor() {
            return getFileDescriptor().findServiceByName(extractServletName(path));
        }
    }

    private final class GrpcServletFileDescriptorSupplier extends GrpcServletBaseDescriptorSupplier {
        GrpcServletFileDescriptorSupplier(String path) {
            super(path);
        }
    }

    private final class GrpcServletMethodDescriptorSupplier extends GrpcServletBaseDescriptorSupplier implements ProtoMethodDescriptorSupplier {
        GrpcServletMethodDescriptorSupplier(String path) {
            super(path);
        }

        @Override
        public Descriptors.MethodDescriptor getMethodDescriptor() {
            return getServiceDescriptor().findMethodByName(METHOD_NAME);
        }
    }
}
