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

package ru.art.rsocket.selector;

import lombok.*;
import ru.art.core.mime.*;

import static io.rsocket.metadata.WellKnownMimeType.*;
import static lombok.AccessLevel.*;
import static ru.art.core.checker.CheckerForEmptiness.*;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.core.extension.NullCheckingExtensions.*;
import static ru.art.rsocket.constants.RsocketModuleConstants.*;
import static ru.art.rsocket.constants.RsocketModuleConstants.RsocketDataFormat.*;
import static ru.art.rsocket.module.RsocketModule.*;

@NoArgsConstructor(access = PRIVATE)
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
        }
        return BINARY_MIME_TYPE;
    }
}
