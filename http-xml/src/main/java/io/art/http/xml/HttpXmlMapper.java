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

package io.art.http.xml;

import io.art.core.mime.*;
import static io.art.core.checker.EmptinessChecker.isEmpty;
import static io.art.core.constants.ArrayConstants.*;
import static io.art.value.immutable.Value.*;
import static io.art.value.constants.ValueType.*;
import static io.art.http.mapper.HttpContentMapper.*;
import static io.art.http.xml.HttpXmlMapperConstants.*;
import static io.art.xml.descriptor.XmlEntityReader.*;
import static io.art.xml.module.XmlModule.*;
import java.nio.charset.*;

public class HttpXmlMapper implements HttpContentToValueMapper, HttpEntityToContentMapper {
    @Override
    public Value mapFromBytes(byte[] bytes, MimeType mimeType, Charset charset) {
        if (isEmpty(bytes)) return null;
        return readXml(xmlModule().configuration().getXmlInputFactory(), new String(bytes, charset));
    }

    @Override
    public byte[] mapToBytes(Value value, MimeType mimeType, Charset charset) {
        if (isEmpty(value)) return EMPTY_BYTES;
        if (value.getType() != XML) throw new HttpXmlMapperException(HTTP_XML_MAPPER_SUPPORT_ONLY_XML_ENTITIES);
        return writeXml(xmlModule().configuration().getXmlOutputFactory(), asXml(value)).getBytes(charset);
    }
}
