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

package ru.art.http.mapper;

import ru.art.entity.CollectionValuesFactory;
import ru.art.entity.Value;
import ru.art.http.exception.HttpTextMapperException;
import ru.art.core.mime.MimeType;
import static java.util.Objects.isNull;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.core.constants.ArrayConstants.EMPTY_BYTES;
import static ru.art.entity.CollectionValuesFactory.emptyCollection;
import static ru.art.entity.Value.asCollection;
import static ru.art.entity.Value.isCollection;
import static ru.art.http.constants.HttpExceptionsMessages.CONTENT_TYPE_IS_NULL;
import java.nio.charset.Charset;

public class HttpBytesMapper implements HttpContentMapper.HttpEntityToContentMapper, HttpContentMapper.HttpContentToValueMapper {
    @Override
    public byte[] mapToBytes(Value value, MimeType mimeType, Charset charset) {
        if (isNull(mimeType)) throw new HttpTextMapperException(CONTENT_TYPE_IS_NULL);
        if (isNull(value)) return EMPTY_BYTES;
        if (!isCollection(value)) return EMPTY_BYTES;
        return asCollection(value).getByteArray();
    }

    @Override
    public Value mapFromBytes(byte[] content, MimeType mimeType, Charset charset) {
        if (isNull(mimeType)) throw new HttpTextMapperException(CONTENT_TYPE_IS_NULL);
        if (isNull(charset)) return emptyCollection();
        if (isEmpty(content)) return emptyCollection();
        return CollectionValuesFactory.byteCollection(content);
    }
}
