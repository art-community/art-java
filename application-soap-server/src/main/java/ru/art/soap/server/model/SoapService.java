/*
 *    Copyright 2019 ART
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package ru.art.soap.server.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;
import ru.art.entity.mapper.ValueFromModelMapper.XmlEntityFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper.XmlEntityToModelMapper;
import ru.art.soap.content.mapper.SoapMimeToContentTypeMapper;
import static ru.art.soap.content.mapper.SoapMimeToContentTypeMapper.textXml;
import java.util.Map;

@Getter
@Builder
public class SoapService {
    @Singular
    private Map<@NonNull String, @NonNull SoapOperation> soapOperations;
    private String serviceUrl;
    private String listeningPath;
    private String wsdlResourcePath;
    private Object defaultFaultResponse;
    private boolean ignoreRequestAcceptType;
    private boolean ignoreRequestContentType;
    private XmlEntityFromModelMapper<?> defaultFaultMapper;
    @Builder.Default
    private final SoapMimeToContentTypeMapper consumes = textXml();
    @Builder.Default
    private final SoapMimeToContentTypeMapper produces = textXml();

    @Getter
    @Builder
    public static class SoapOperation {
        private XmlEntityToModelMapper<?> requestMapper;
        private XmlEntityFromModelMapper<?> responseMapper;
        @Singular("faultMapper")
        private Map<Class<? extends Exception>, XmlEntityFromModelMapper<?>> faultMapping;
        private String methodId;
    }

}
