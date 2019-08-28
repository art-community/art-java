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

package ru.art.soap.client.configuration;

import lombok.*;
import ru.art.core.module.*;
import static ru.art.soap.client.constants.SoapClientModuleConstants.*;

public interface SoapClientModuleConfiguration extends ModuleConfiguration {
    String getEnvelopePrefix();

    String getEnvelopeNamespace();

    String getBodyPrefix();

    String getBodyNamespace();

    SoapClientModuleDefaultConfiguration DEFAULT_CONFIGURATION = new SoapClientModuleDefaultConfiguration();

	@Getter
	class SoapClientModuleDefaultConfiguration implements SoapClientModuleConfiguration {
        private final String envelopePrefix = SOAP_ENVELOPE_DEFAULT_PREFIX;
        private final String envelopeNamespace = SOAP_ENVELOPE_DEFAULT_NAMESPACE;
        private final String bodyPrefix = SOAP_BODY_DEFAULT_PREFIX;
        private final String bodyNamespace = SOAP_BODY_DEFAULT_NAMESPACE;
    }
}
