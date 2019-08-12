/*
 *    Copyright 2019 ART
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package ru.art.rsocket.reader;

import io.rsocket.Payload;
import lombok.NoArgsConstructor;
import ru.art.entity.Value;
import ru.art.rsocket.exception.RsocketException;
import ru.art.rsocket.exception.RsocketServerException;
import static java.text.MessageFormat.format;
import static lombok.AccessLevel.PRIVATE;
import static ru.art.core.wrapper.ExceptionWrapper.wrapException;
import static ru.art.json.descriptor.JsonEntityReader.readJson;
import static ru.art.protobuf.descriptor.ProtobufEntityReader.readProtobuf;
import static ru.art.protobuf.entity.ProtobufValueMessage.ProtobufValue.parseFrom;
import static ru.art.rsocket.constants.RsocketModuleConstants.ExceptionMessages.UNSUPPORTED_DATA_FORMAT;
import static ru.art.rsocket.constants.RsocketModuleConstants.RsocketDataFormat;
import static ru.art.rsocket.module.RsocketModule.rsocketModule;
import static ru.art.xml.descriptor.XmlEntityReader.readXml;


@NoArgsConstructor(access = PRIVATE)
public class RsocketPayloadReader {
    public static Value readPayload(Payload payload, RsocketDataFormat dataFormat) {
        switch (dataFormat) {
            case PROTOBUF:
                return readProtobuf(wrapException(() -> parseFrom(payload.getData()), RsocketServerException::new));
            case JSON:
                return readJson(wrapException(payload::getDataUtf8, RsocketServerException::new));
            case XML:
                return readXml(wrapException(payload::getDataUtf8, RsocketServerException::new));
        }
        throw new RsocketException(format(UNSUPPORTED_DATA_FORMAT, rsocketModule().getDefaultDataFormat()));
    }
}
