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

package io.art.rsocket.communicator;

import io.art.server.constants.*;
import io.rsocket.*;
import io.rsocket.core.*;
import io.rsocket.transport.netty.client.*;
import lombok.*;

@RequiredArgsConstructor
public class RsocketCommunicatorClient {
    ServerModuleConstants.ServiceMethodProcessingMode requestPayloadType;
    ServerModuleConstants.ServiceMethodProcessingMode responsePayloadType;
    RSocketClient client = RSocketConnector.createRSocketClient(TcpClientTransport.create(123));

    public Object execute(Object request) {
        switch (requestPayloadType) {
            case BLOCKING:
                break;
            case MONO:
                break;
            case FLUX:
                break;
        }
        switch (responsePayloadType) {
            case BLOCKING:
                break;
            case MONO:
                break;
            case FLUX:
                break;
        }
        return null;
    }
}
