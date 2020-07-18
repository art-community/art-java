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

package io.art.rsocket.writer;

import io.art.entity.immutable.*;
import io.rsocket.*;
import lombok.experimental.*;
import io.art.rsocket.constants.RsocketModuleConstants.*;
import io.art.rsocket.exception.*;
import static io.rsocket.util.DefaultPayload.*;
import static java.text.MessageFormat.*;
import static io.art.entity.immutable.Value.*;
import static io.art.json.descriptor.JsonEntityWriter.*;
import static io.art.message.pack.descriptor.MessagePackEntityWriter.*;
import static io.art.protobuf.descriptor.ProtobufEntityWriter.*;
import static io.art.rsocket.constants.RsocketModuleConstants.ExceptionMessages.*;
import static io.art.rsocket.module.RsocketModule.*;
import static io.art.xml.descriptor.XmlEntityWriter.*;

@UtilityClass
public class RsocketPayloadWriter {
    public static Payload writePayloadData(Value value, RsocketDataFormat dataFormat) {
        switch (dataFormat) {
            case PROTOBUF:
                return create(writeProtobufToBytes(value));
            case JSON:
                return create(writeJsonToBytes(value));
            case XML:
                return create(writeXmlToBytes(asXml(value)));
            case MESSAGE_PACK:
                return create(writeMessagePackToBytes(value));

        }
        throw new RsocketException(format(UNSUPPORTED_DATA_FORMAT, rsocketModule().getDataFormat()));
    }

    public static Payload writePayloadMetaData(Value dataValue, Value metadataValue, RsocketDataFormat dataFormat) {
        switch (dataFormat) {
            case PROTOBUF:
                return create(writeProtobufToBytes(dataValue), writeProtobufToBytes(metadataValue));
            case JSON:
                return create(writeJsonToBytes(dataValue), writeJsonToBytes(metadataValue));
            case XML:
                return create(writeXmlToBytes(asXml(dataValue)), writeXmlToBytes(asXml(metadataValue)));
            case MESSAGE_PACK:
                return create(writeMessagePackToBytes(dataValue), writeMessagePackToBytes(metadataValue));

        }
        throw new RsocketException(format(UNSUPPORTED_DATA_FORMAT, rsocketModule().getDataFormat()));
    }
}
