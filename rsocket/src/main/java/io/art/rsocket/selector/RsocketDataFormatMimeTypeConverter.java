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

package io.art.rsocket.selector;

import lombok.experimental.*;
import io.art.core.mime.*;
import static io.rsocket.metadata.WellKnownMimeType.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.message.pack.constants.MessagePackConstants.*;
import static io.art.rsocket.constants.RsocketModuleConstants.*;
import static io.art.rsocket.constants.RsocketModuleConstants.RsocketDataFormat.*;
import static io.art.rsocket.module.RsocketModule.*;

@UtilityClass
public class RsocketDataFormatMimeTypeConverter {
    public static RsocketDataFormat fromMimeType(String mimeType) {
        if (isEmpty(mimeType)) {
            return rsocketModule().getDataFormat();
        }
        MimeType type = MimeType.valueOf(mimeType);
        if (APPLICATION_JSON.getString().equals(type.getType() + SLASH + type.getSubtype())) return JSON;
        if (APPLICATION_PROTOBUF.getString().equals(type.getType() + SLASH + type.getSubtype())) return PROTOBUF;
        if (APPLICATION_XML.getString().equals(type.getType() + SLASH + type.getSubtype())) return XML;
        if (TEXT_XML.getString().equals(type.getType() + SLASH + type.getSubtype())) return XML;
        if (mimeType.equalsIgnoreCase(APPLICATION_MESSAGE_PACK.getType() + SLASH + APPLICATION_MESSAGE_PACK.getSubtype())) {
            return MESSAGE_PACK;
        }
        return rsocketModule().getDataFormat();
    }

    public static String toMimeType(RsocketDataFormat dataFormat) {
        switch (getOrElse(dataFormat, rsocketModule().getDataFormat())) {
            case PROTOBUF:
                return APPLICATION_PROTOBUF.getString();
            case JSON:
                return APPLICATION_JSON.getString();
            case XML:
                return APPLICATION_XML.getString();
            case MESSAGE_PACK:
                return APPLICATION_MESSAGE_PACK.getType() + SLASH + APPLICATION_MESSAGE_PACK.getSubtype();
        }
        return BINARY_MIME_TYPE;
    }
}
