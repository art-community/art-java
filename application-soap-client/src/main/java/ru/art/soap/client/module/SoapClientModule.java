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

package ru.art.soap.client.module;

import lombok.*;
import ru.art.core.module.*;
import ru.art.soap.client.configuration.*;
import ru.art.soap.client.configuration.SoapClientModuleConfiguration.*;

import static lombok.AccessLevel.*;
import static ru.art.core.context.Context.*;
import static ru.art.soap.client.constants.SoapClientModuleConstants.*;

@Getter
public class SoapClientModule implements Module<SoapClientModuleConfiguration, ModuleState> {
    @Getter(lazy = true, value = PRIVATE)
    private static final SoapClientModuleConfiguration soapClientModule = context().getModule(SOAP_CLIENT_MODULE_ID, SoapClientModule::new);
    private final String id = SOAP_CLIENT_MODULE_ID;
    private final SoapClientModuleConfiguration defaultConfiguration = SoapClientModuleDefaultConfiguration.DEFAULT_CONFIGURATION;

    public static SoapClientModuleConfiguration soapClientModule() {
        if (insideDefaultContext()) {
            return SoapClientModuleConfiguration.DEFAULT_CONFIGURATION;
        }
        return getSoapClientModule();
    }
}
