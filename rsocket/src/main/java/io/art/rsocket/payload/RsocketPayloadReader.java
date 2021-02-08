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

package io.art.rsocket.payload;

import io.art.core.exception.*;
import io.art.rsocket.model.*;
import io.art.value.constants.ValueModuleConstants.*;
import io.netty.buffer.*;
import io.rsocket.*;
import lombok.*;
import static io.art.json.descriptor.JsonReader.*;
import static io.art.message.pack.descriptor.MessagePackReader.*;
import static io.art.protobuf.descriptor.ProtobufReader.*;
import static io.art.rsocket.model.RsocketPayloadValue.*;
import static io.art.xml.descriptor.XmlReader.*;
import static io.art.yaml.descriptor.YamlReader.*;

@RequiredArgsConstructor
public class RsocketPayloadReader {
    private final DataFormat dataFormat;
    private final DataFormat metaDataFormat;

    public RsocketPayloadReader(RsocketSetupPayload setupPayload) {
        this.dataFormat = setupPayload.getDataFormat();
        this.metaDataFormat = setupPayload.getMetadataFormat();
    }

    public RsocketPayloadValue readPayloadData(Payload payload) {
        ByteBuf data = payload.sliceData();
        return read(payload, data, dataFormat);
    }

    public RsocketPayloadValue readPayloadMetaData(Payload payload) {
        ByteBuf data = payload.sliceMetadata();
        return read(payload, data, metaDataFormat);
    }

    private RsocketPayloadValue read(Payload payload, ByteBuf data, DataFormat format) {
        if (data.capacity() == 0) return emptyRsocketPayload();
        switch (format) {
            case PROTOBUF:
                return new RsocketPayloadValue(payload, readProtobuf(data));
            case JSON:
                return new RsocketPayloadValue(payload, readJson(data));
            case YAML:
                return new RsocketPayloadValue(payload, readYaml(data));
            case XML:
                return new RsocketPayloadValue(payload, readXml(data));
            case MESSAGE_PACK:
                return new RsocketPayloadValue(payload, readMessagePack(data));
        }
        throw new ImpossibleSituationException();
    }
}
