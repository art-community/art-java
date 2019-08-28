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

package ru.art.soap.content.mapper;

import ru.art.core.mime.*;
import ru.art.http.mapper.*;
import ru.art.http.xml.*;
import static ru.art.core.factory.CollectionsFactory.*;
import static ru.art.http.constants.HttpMimeTypes.*;
import java.util.*;

public interface HttpSoapMappingConfigurator {
    static Map<MimeType, HttpContentMapper> configureSoapHttpMappers(Map<MimeType, HttpContentMapper> defaultMappers) {
        HttpXmlMapper httpXmlMapper = new HttpXmlMapper();
        HttpContentMapper httpSoapMapper = new HttpContentMapper(httpXmlMapper, httpXmlMapper);
        return mapOf(defaultMappers)
                .add(APPLICATION_SOAP_XML, httpSoapMapper)
                .add(APPLICATION_XML, httpSoapMapper)
                .add(TEXT_XML, httpSoapMapper);
    }

}
