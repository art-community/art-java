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

import io.art.entity.immutable.Value;
import io.art.entity.mapper.*;
import io.netty.buffer.*;
import io.rsocket.*;
import lombok.*;
import static io.art.message.pack.descriptor.MessagePackEntityReader.*;
import static io.art.message.pack.descriptor.MessagePackEntityWriter.*;
import static io.rsocket.util.ByteBufPayload.*;

@RequiredArgsConstructor
public class RsocketCommunicatorClient {
    private final RSocket rSocket;
    private final ValueFromModelMapper<Object, Value> inputMapper;
    private final ValueToModelMapper<Object, Value> outputMapper;

    public void fireAndForget(Object request) {

    }

    public Object requestResponse(Object request) {
        return rSocket
                .requestResponse(create(writeMessagePackToBytes(inputMapper.map(request))))
                .map(payload -> outputMapper.map(readMessagePack(new ByteBufInputStream(payload.data()))));
    }
}
