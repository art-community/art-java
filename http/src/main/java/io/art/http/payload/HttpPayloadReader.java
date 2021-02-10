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

package io.art.http.payload;

import io.art.core.exception.*;
import io.art.http.model.*;
import io.art.value.constants.ValueModuleConstants.*;
import io.netty.buffer.*;
import lombok.experimental.*;
import static io.art.http.model.HttpPayloadValue.*;
import static io.art.json.descriptor.JsonReader.*;
import static io.art.message.pack.descriptor.MessagePackReader.*;
import static io.art.protobuf.descriptor.ProtobufReader.*;
import static io.art.xml.descriptor.XmlReader.*;
import static io.art.yaml.descriptor.YamlReader.*;

@UtilityClass
public class HttpPayloadReader {
    public HttpPayloadValue readPayloadData(DataFormat dataFormat, ByteBuf payload) {
        return read(payload, dataFormat);
    }

    private HttpPayloadValue read(ByteBuf payload, DataFormat format) {
        if (payload.capacity() == 0) return emptyHttpPayload();
        switch (format) {
            case PROTOBUF:
                return new HttpPayloadValue(payload, readProtobuf(payload));
            case JSON:
                return new HttpPayloadValue(payload, readJson(payload));
            case YAML:
                return new HttpPayloadValue(payload, readYaml(payload));
            case XML:
                return new HttpPayloadValue(payload, readXml(payload));
            case MESSAGE_PACK:
                return new HttpPayloadValue(payload, readMessagePack(payload));
        }
        throw new ImpossibleSituationException();
    }
}
