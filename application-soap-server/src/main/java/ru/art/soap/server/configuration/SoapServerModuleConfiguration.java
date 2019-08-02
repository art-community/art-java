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

package ru.art.soap.server.configuration;

import lombok.Getter;
import ru.art.core.module.ModuleConfiguration;
import ru.art.entity.mapper.ValueFromModelMapper.XmlEntityFromModelMapper;
import ru.art.soap.server.model.SoapFault;
import static ru.art.soap.server.constans.SoapServerModuleConstants.ResponseFaultConstants.UNEXPECTED_ERROR;
import static ru.art.soap.server.constans.SoapServerModuleConstants.ResponseFaultConstants.UNEXPECTED_ERROR_TEXT;
import static ru.art.soap.server.mapper.SoapMapper.soapResponseFaultMapper;

public interface SoapServerModuleConfiguration extends ModuleConfiguration {
    <T extends Exception> T getDefaultFaultResponse();

    <T extends Exception> XmlEntityFromModelMapper<T> getDefaultFaultMapper();

    @Getter
    class SoapServerModuleDefaultConfiguration implements SoapServerModuleConfiguration {
        private SoapFault defaultFaultResponse = SoapFault.builder()
                .codeValue(UNEXPECTED_ERROR)
                .reasonText(UNEXPECTED_ERROR_TEXT)
                .build();
        private XmlEntityFromModelMapper<SoapFault> defaultFaultMapper = soapResponseFaultMapper;
    }
}
