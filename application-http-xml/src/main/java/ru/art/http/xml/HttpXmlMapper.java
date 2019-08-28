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

package ru.art.http.xml;

import ru.art.core.mime.*;
import ru.art.entity.*;
import ru.art.http.exception.*;
import static java.util.Objects.*;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.core.constants.ArrayConstants.*;
import static ru.art.entity.Value.*;
import static ru.art.entity.XmlEntity.*;
import static ru.art.entity.constants.ValueType.*;
import static ru.art.http.constants.HttpExceptionsMessages.*;
import static ru.art.http.mapper.HttpContentMapper.*;
import static ru.art.http.xml.HttpXmlMapperConstants.*;
import static ru.art.xml.descriptor.XmlEntityReader.*;
import static ru.art.xml.descriptor.XmlEntityWriter.*;
import static ru.art.xml.module.XmlModule.*;
import java.nio.charset.*;

public class HttpXmlMapper implements HttpContentToValueMapper, HttpEntityToContentMapper {
    @Override
    public Value mapFromBytes(byte[] bytes, MimeType mimeType, Charset charset) {
        if (isNull(mimeType)) throw new HttpTextMapperException(CONTENT_TYPE_IS_NULL);
        if (isEmpty(bytes)) return xmlEntityBuilder().create();
        return readXml(xmlModule().getXmlInputFactory(), new String(bytes, charset));
    }

    @Override
    public byte[] mapToBytes(Value value, MimeType mimeType, Charset charset) {
        if (isNull(mimeType)) throw new HttpTextMapperException(CONTENT_TYPE_IS_NULL);
        if (isEmpty(value)) return EMPTY_BYTES;
        if (value.getType() != XML_ENTITY) throw new HttpXmlMapperException(HTTP_XML_MAPPER_SUPPORT_ONLY_XML_ENTITIES);
        return writeXml(xmlModule().getXmlOutputFactory(), asXmlEntity(value)).getBytes(charset);
    }
}
