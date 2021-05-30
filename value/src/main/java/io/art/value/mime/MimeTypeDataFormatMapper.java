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

package io.art.value.mime;

import io.art.core.mime.*;
import io.art.value.constants.ValueModuleConstants.*;
import io.art.value.exception.*;
import lombok.experimental.*;
import static io.art.core.constants.ExceptionMessages.*;
import static io.art.core.constants.MimeTypeConstants.*;
import static io.art.core.mime.MimeTypes.*;
import static io.art.value.constants.ValueModuleConstants.DataFormat.*;
import java.nio.charset.*;

@UtilityClass
public class MimeTypeDataFormatMapper {
    public static DataFormat fromMimeType(MimeType type) {
        if (APPLICATION_JSON.equals(type)) return JSON;
        if (APPLICATION_PROTOBUF.equals(type)) return PROTOBUF;
        if (APPLICATION_MESSAGE_PACK.equals(type)) return MESSAGE_PACK;
        if (APPLICATION_YAML.equals(type)) return YAML;
        if (APPLICATION_YML.equals(type)) return YAML;
        throw new UnsupportedMimeTypeException(type);
    }

    public static DataFormat fromMimeType(MimeType type, DataFormat fallback) {
        if (APPLICATION_JSON.equals(type)) return JSON;
        if (APPLICATION_PROTOBUF.equals(type)) return PROTOBUF;
        if (APPLICATION_MESSAGE_PACK.equals(type)) return MESSAGE_PACK;
        if (APPLICATION_YAML.equals(type)) return YAML;
        if (APPLICATION_YML.equals(type)) return YAML;
        return fallback;
    }

    public static MimeType toMimeType(DataFormat dataFormat) {
        switch (dataFormat) {
            case PROTOBUF:
                return APPLICATION_PROTOBUF;
            case JSON:
                return APPLICATION_JSON;
            case MESSAGE_PACK:
                return APPLICATION_MESSAGE_PACK;
            case YAML:
                return APPLICATION_YAML;
        }
        throw new IllegalArgumentException(DATA_FORMAT_IS_NULL);
    }

    public static MimeType toMimeType(DataFormat dataFormat, Charset charset) {
        switch (dataFormat) {
            case PROTOBUF:
                return APPLICATION_PROTOBUF.withParameter(PARAM_CHARSET, charset.name());
            case JSON:
                return APPLICATION_JSON.withParameter(PARAM_CHARSET, charset.name());
            case MESSAGE_PACK:
                return APPLICATION_MESSAGE_PACK.withParameter(PARAM_CHARSET, charset.name());
            case YAML:
                return APPLICATION_YAML.withParameter(PARAM_CHARSET, charset.name());
        }
        throw new IllegalArgumentException(DATA_FORMAT_IS_NULL);
    }
}
