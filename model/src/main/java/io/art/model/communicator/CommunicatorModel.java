/*
 * ART
 *
 * Copyright 2020 ART
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

package io.art.model.communicator;

import lombok.*;
import static java.util.function.UnaryOperator.identity;
import java.util.function.*;

@Getter
public class CommunicatorModel {
    private GrpcCommunicatorModel grpcModel;
    private HttpCommunicatorModel httpModel;
    private RsocketCommunicatorModel rsocketModel;
    private SoapCommunicatorModel soapModel;

    public CommunicatorModel grpc(String name, Class<?> specification) {
        return grpc(name, specification, identity());
    }

    public CommunicatorModel http(String name, Class<?> specification) {
        return http(name, specification, identity());
    }

    public CommunicatorModel rsocket(String name, Class<?> specification) {
        return rsocket(name, specification, identity());
    }

    public CommunicatorModel soap(String name, Class<?> specification) {
        return soap(name, specification, identity());
    }

    public CommunicatorModel grpc(String name, Class<?> specification, UnaryOperator<GrpcCommunicatorModel> communicator) {
        grpcModel = communicator.apply(new GrpcCommunicatorModel(name, specification));
        return this;
    }

    public CommunicatorModel http(String name, Class<?> specification, UnaryOperator<HttpCommunicatorModel> communicator) {
        httpModel = communicator.apply(new HttpCommunicatorModel(name, specification));
        return this;
    }

    public CommunicatorModel rsocket(String name, Class<?> specification, UnaryOperator<RsocketCommunicatorModel> communicator) {
        rsocketModel = communicator.apply(new RsocketCommunicatorModel(name, specification));
        return this;
    }

    public CommunicatorModel soap(String name, Class<?> specification, UnaryOperator<SoapCommunicatorModel> communicator) {
        soapModel = communicator.apply(new SoapCommunicatorModel(name, specification));
        return this;
    }
}
