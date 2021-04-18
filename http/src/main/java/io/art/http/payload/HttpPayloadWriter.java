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
import io.art.value.immutable.*;
import io.netty.buffer.*;
import lombok.experimental.*;
import static io.art.core.extensions.NettyBufferExtensions.*;
import static io.art.json.descriptor.JsonWriter.*;
import static io.art.message.pack.descriptor.MessagePackWriter.*;
import static io.art.protobuf.descriptor.ProtobufWriter.*;
import static io.art.value.constants.ValueModuleConstants.*;
import static io.art.value.constants.ValueModuleConstants.ValueType.*;
import static io.art.value.immutable.Value.*;
import static io.art.value.xml.XmlEntityFromEntityConverter.*;
import static io.art.xml.descriptor.XmlWriter.*;
import static io.art.yaml.descriptor.YamlWriter.*;

@UtilityClass
public class HttpPayloadWriter {
    public ByteBuf writePayloadData(DataFormat dataFormat, Value value) {
        switch (dataFormat) {
            case PROTOBUF:
                return from(writeProtobufToBytes(value));
            case JSON:
                return from(writeJsonToBytes(value));
            case XML:
                return from(writeXmlToBytes(value.getType() == XML ? asXml(value) : fromEntityAsTags(asEntity(value))));
            case MESSAGE_PACK:
                return from(writeMessagePackToBytes(value));
            case YAML:
                return from(writeYamlToBytes(value));
        }
        throw new ImpossibleSituationException();
    }
}
