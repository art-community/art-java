/*
 * ART Java
 *
 * Copyright 2019 ART
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

package ru.art.rsocket.writer;

import io.rsocket.Payload;
import lombok.NoArgsConstructor;
import ru.art.entity.Value;
import ru.art.rsocket.constants.RsocketModuleConstants.RsocketDataFormat;
import ru.art.rsocket.exception.RsocketException;
import static io.rsocket.util.DefaultPayload.create;
import static java.text.MessageFormat.format;
import static lombok.AccessLevel.PRIVATE;
import static ru.art.entity.Value.asXmlEntity;
import static ru.art.json.descriptor.JsonEntityWriter.writeJson;
import static ru.art.protobuf.descriptor.ProtobufEntityWriter.writeProtobuf;
import static ru.art.rsocket.constants.RsocketModuleConstants.ExceptionMessages.UNSUPPORTED_DATA_FORMAT;
import static ru.art.rsocket.module.RsocketModule.rsocketModule;
import static ru.art.xml.descriptor.XmlEntityWriter.writeXml;

@NoArgsConstructor(access = PRIVATE)
public class RsocketPayloadWriter {
    public static Payload writePayload(Value value, RsocketDataFormat dataFormat) {
        switch (dataFormat) {
            case PROTOBUF:
                return create(writeProtobuf(value).toByteArray());
            case JSON:
                return create(writeJson(value));
            case XML:
                return create(writeXml(asXmlEntity(value)));
        }
        throw new RsocketException(format(UNSUPPORTED_DATA_FORMAT, rsocketModule().getDefaultDataFormat()));
    }
}
