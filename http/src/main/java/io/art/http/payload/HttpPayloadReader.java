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

package io.art.http.payload;

import io.art.core.exception.*;
import io.art.http.model.*;
import io.art.json.descriptor.*;
import io.art.message.pack.descriptor.*;
import io.art.protobuf.descriptor.*;
import io.art.value.constants.ValueModuleConstants.*;
import io.art.xml.descriptor.*;
import io.art.yaml.descriptor.*;
import io.netty.buffer.*;
import lombok.experimental.*;
import static io.art.http.model.HttpPayloadValue.*;
import static io.art.json.module.JsonModule.*;
import static io.art.message.pack.module.MessagePackModule.*;
import static io.art.protobuf.module.ProtobufModule.*;
import static io.art.xml.module.XmlModule.*;
import static io.art.yaml.module.YamlModule.*;

@UtilityClass
public class HttpPayloadReader {
    private static final ProtobufReader protobufReader = protobufModule().configuration().getReader();
    private static final JsonReader jsonReader = jsonModule().configuration().getReader();
    private static final YamlReader yamlReader = yamlModule().configuration().getReader();
    private static final XmlReader xmlReader = xmlModule().configuration().getReader();
    private static final MessagePackReader messagePackReader = messagePackModule().configuration().getReader();

    public HttpPayloadValue readPayloadData(DataFormat dataFormat, ByteBuf payload) {
        return read(dataFormat, payload);
    }

    private HttpPayloadValue read(DataFormat format, ByteBuf payload) {
        if (payload.capacity() == 0) return emptyHttpPayload();
        switch (format) {
            case PROTOBUF:
                return new HttpPayloadValue(payload, protobufReader.read(payload));
            case JSON:
                return new HttpPayloadValue(payload, jsonReader.read(payload));
            case YAML:
                return new HttpPayloadValue(payload, yamlReader.read(payload));
            case XML:
                return new HttpPayloadValue(payload, xmlReader.read(payload));
            case MESSAGE_PACK:
                return new HttpPayloadValue(payload, messagePackReader.read(payload));
        }
        throw new ImpossibleSituationException();
    }
}
