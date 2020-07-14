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

package io.art.http.mapper;

import io.art.core.mime.*;
import io.art.entity.*;
import io.art.http.exception.*;
import io.art.http.mapper.HttpContentMapper.*;
import static java.util.Objects.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.constants.ArrayConstants.*;
import static io.art.entity.PrimitivesFactory.*;
import static io.art.http.constants.HttpExceptionsMessages.*;
import java.nio.charset.*;

public class HttpTextPlainMapper implements HttpEntityToContentMapper, HttpContentToValueMapper {
    @Override
    public byte[] mapToBytes(Value value, MimeType mimeType, Charset charset) {
        if (isNull(mimeType)) throw new HttpTextMapperException(CONTENT_TYPE_IS_NULL);
        if (isNull(value)) return EMPTY_BYTES;
        return value.toString().getBytes(charset);
    }

    @Override
    public Value mapFromBytes(byte[] content, MimeType mimeType, Charset charset) {
        if (isNull(mimeType)) throw new HttpTextMapperException(CONTENT_TYPE_IS_NULL);
        if (isEmpty(content)) return null;
        return stringPrimitive(new String(content, charset));
    }
}
