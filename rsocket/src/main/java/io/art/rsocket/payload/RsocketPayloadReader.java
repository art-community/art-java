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

import io.art.entity.constants.EntityConstants.*;
import io.art.entity.immutable.*;
import io.art.rsocket.exception.*;
import io.art.rsocket.model.*;
import io.netty.buffer.*;
import io.rsocket.*;
import lombok.experimental.*;
import static io.art.core.wrapper.ExceptionWrapper.*;
import static io.art.json.descriptor.JsonEntityReader.*;
import static io.art.message.pack.descriptor.MessagePackEntityReader.*;
import static io.art.protobuf.descriptor.ProtobufEntityReader.*;
import static io.art.rsocket.constants.RsocketModuleConstants.ExceptionMessages.*;
import static io.art.rsocket.module.RsocketModule.*;
import static io.art.xml.descriptor.XmlEntityReader.*;
import static java.text.MessageFormat.*;
import java.nio.*;

@UtilityClass
public class RsocketPayloadReader {
    public static RsocketPayloadValue readPayloadData(Payload payload, DataFormat dataFormat) {
        ByteBuffer data = payload.getData();
        if (data.capacity() == 0) {
            return null;
        }
        switch (dataFormat) {
            case PROTOBUF:
                return new RsocketPayloadValue(payload, readProtobuf(data));
            case JSON:
                return new RsocketPayloadValue(payload, readJson(data));
            case XML:
                return readXml(wrapException(payload::getDataUtf8, RsocketServerException::new));
            case MESSAGE_PACK:
                ByteBuf byteBuf = payload.sliceData();
                if (byteBuf.readableBytes() == 0) {
                    return null;
                }
                byte[] bytes = new byte[byteBuf.readableBytes()];
                byteBuf.readBytes(bytes);
                return readMessagePack(bytes);
        }
        throw new RsocketException(format(UNSUPPORTED_DATA_FORMAT, rsocketModule().configuration().getDataFormat()));
    }

    public static RsocketPayloadValue readPayloadMetaData(Payload payload, DataFormat dataFormat) {
        switch (dataFormat) {
            case PROTOBUF:
                ByteBuffer metadata = payload.getMetadata();
                if (metadata.capacity() == 0) {
                    return null;
                }
                return readProtobuf(wrapException(() -> com.google.protobuf.Value.parseFrom(metadata), RsocketServerException::new));
            case JSON:
                return readJson(wrapException(payload::getMetadataUtf8, RsocketServerException::new));
            case XML:
                return readXml(wrapException(payload::getMetadataUtf8, RsocketServerException::new));
            case MESSAGE_PACK:
                ByteBuf byteBuf = payload.sliceMetadata();
                if (byteBuf.readableBytes() == 0) {
                    return null;
                }
                byte[] bytes = new byte[byteBuf.readableBytes()];
                byteBuf.readBytes(bytes);
                return readMessagePack(bytes);
        }
        throw new RsocketException(format(UNSUPPORTED_DATA_FORMAT, rsocketModule().configuration().getDataFormat()));
    }
}
