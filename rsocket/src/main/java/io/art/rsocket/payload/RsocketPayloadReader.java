/*
 * ART
 *
 * Copyright 2019-2021 ART
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
import io.art.json.descriptor.*;
import io.art.message.pack.descriptor.*;
import io.art.protobuf.descriptor.*;
import io.art.rsocket.model.*;
import io.art.value.constants.ValueModuleConstants.*;
import io.art.xml.descriptor.*;
import io.art.yaml.descriptor.*;
import io.netty.buffer.*;
import io.rsocket.*;
import lombok.*;
import static io.art.json.module.JsonModule.*;
import static io.art.message.pack.module.MessagePackModule.*;
import static io.art.protobuf.module.ProtobufModule.*;
import static io.art.rsocket.model.RsocketPayloadValue.*;
import static io.art.xml.module.XmlModule.*;
import static io.art.yaml.module.YamlModule.*;

@RequiredArgsConstructor
public class RsocketPayloadReader {
    private final DataFormat dataFormat;
    private final DataFormat metaDataFormat;
    private static final ProtobufReader protobufReader = protobufModule().configuration().getReader();
    private static final JsonReader jsonReader = jsonModule().configuration().getReader();
    private static final YamlReader yamlReader = yamlModule().configuration().getReader();
    private static final XmlReader xmlReader = xmlModule().configuration().getReader();
    private static final MessagePackReader messagePackReader = messagePackModule().configuration().getReader();

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
                return new RsocketPayloadValue(payload, protobufReader.read(data));
            case JSON:
                return new RsocketPayloadValue(payload, jsonReader.read(data));
            case YAML:
                return new RsocketPayloadValue(payload, yamlReader.read(data));
            case XML:
                return new RsocketPayloadValue(payload, xmlReader.read(data));
            case MESSAGE_PACK:
                return new RsocketPayloadValue(payload, messagePackReader.read(data));
        }
        throw new ImpossibleSituationException();
    }
}
