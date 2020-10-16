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

import io.art.communicator.implementation.*;
import io.art.entity.immutable.Value;
import io.art.rsocket.constants.RsocketModuleConstants.*;
import io.art.rsocket.model.*;
import io.art.rsocket.payload.*;
import io.rsocket.core.*;
import lombok.*;
import org.apache.logging.log4j.*;
import reactor.core.publisher.*;
import static io.art.logging.LoggingModule.*;
import static lombok.AccessLevel.*;

public class RsocketCommunicator implements CommunicatorImplementation {
    @Getter(lazy = true, value = PRIVATE)
    private final static Logger logger = logger(RsocketCommunicator.class);
    private final RSocketClient client;
    private final RsocketPayloadWriter writer;
    private final RsocketPayloadReader reader;
    private final CommunicationMode communicationMode;

    public RsocketCommunicator(RSocketClient client, CommunicationMode communicationMode, RsocketSetupPayload setupPayload) {
        this.communicationMode = communicationMode;
        this.client = client;
        reader = new RsocketPayloadReader(setupPayload.getDataFormat(), setupPayload.getMetadataFormat());
        writer = new RsocketPayloadWriter(setupPayload.getDataFormat(), setupPayload.getMetadataFormat());
    }

    @Override
    public Flux<Value> communicate(Flux<Value> input) {
        switch (communicationMode) {
            case FIRE_AND_FORGET:
                return client.fireAndForget(input.map(writer::writePayloadData).last())
                        .flux()
                        .cast(Value.class);
            case REQUEST_RESPONSE:
                return client.requestResponse(input.map(writer::writePayloadData).last())
                        .flux()
                        .map(payload -> reader.readPayloadData(payload).getValue());
            case REQUEST_STREAM:
                return client.requestStream(input.map(writer::writePayloadData).last())
                        .map(payload -> reader.readPayloadData(payload).getValue());
            case REQUEST_CHANNEL:
                return client.requestChannel(input.map(writer::writePayloadData))
                        .map(payload -> reader.readPayloadData(payload).getValue());
            case METADATA_PUSH:
                return client.metadataPush(input.map(writer::writePayloadData).last())
                        .flux()
                        .cast(Value.class);
        }
        return Flux.never();
    }
}
