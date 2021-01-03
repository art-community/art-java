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

package io.art.value.mime;

import io.art.core.mime.*;
import io.art.value.constants.ValueModuleConstants.*;
import io.art.value.exception.*;
import lombok.experimental.*;
import static io.art.core.constants.ExceptionMessages.*;
import static io.art.core.mime.MimeTypes.*;
import static io.art.value.constants.ValueModuleConstants.DataFormat.*;
import static java.text.MessageFormat.*;

@UtilityClass
public class MimeTypeDataFormatMapper {
    public static DataFormat fromMimeType(MimeType type) {
        if (APPLICATION_JSON.equals(type)) return JSON;
        if (APPLICATION_PROTOBUF.equals(type)) return PROTOBUF;
        if (APPLICATION_XML.equals(type)) return XML;
        if (TEXT_XML.equals(type)) return XML;
        if (APPLICATION_MESSAGE_PACK.equals(type)) return MESSAGE_PACK;
        throw new UnsupportedMimeTypeException(type);
    }

    public static DataFormat fromMimeType(MimeType type, DataFormat fallback) {
        if (APPLICATION_JSON.equals(type)) return JSON;
        if (APPLICATION_PROTOBUF.equals(type)) return PROTOBUF;
        if (APPLICATION_XML.equals(type)) return XML;
        if (TEXT_XML.equals(type)) return XML;
        if (APPLICATION_MESSAGE_PACK.equals(type)) return MESSAGE_PACK;
        return fallback;
    }

    public static MimeType toMimeType(DataFormat dataFormat) {
        switch (dataFormat) {
            case PROTOBUF:
                return APPLICATION_PROTOBUF;
            case JSON:
                return APPLICATION_JSON;
            case XML:
                return APPLICATION_XML;
            case MESSAGE_PACK:
                return APPLICATION_MESSAGE_PACK;
        }
        throw new IllegalArgumentException(format(ARGUMENT_IS_NULL, "dataFormat"));
    }
}
