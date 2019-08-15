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

package ru.art.soap.server.module;

import lombok.Getter;
import ru.art.core.module.Module;
import ru.art.core.module.ModuleState;
import ru.art.soap.server.configuration.SoapServerModuleConfiguration;
import ru.art.soap.server.configuration.SoapServerModuleConfiguration.SoapServerModuleDefaultConfiguration;
import static ru.art.core.context.Context.context;
import static ru.art.soap.server.constans.SoapServerModuleConstants.SOAP_SERVER_MODULE_ID;

@Getter
public class SoapServerModule implements Module<SoapServerModuleConfiguration, ModuleState> {
    @Getter(lazy = true, value = PRIVATE)
    private final static SoapServerModuleConfiguration soapServerModule = context().getModule(SOAP_SERVER_MODULE_ID, SoapServerModule::new);
    private final String id = SOAP_SERVER_MODULE_ID;
    private final SoapServerModuleConfiguration defaultConfiguration = new SoapServerModuleDefaultConfiguration();

    public static SoapServerModuleConfiguration soapServerModule() {
        return context().getModule(SOAP_SERVER_MODULE_ID, SoapServerModule::new);
    }

}
