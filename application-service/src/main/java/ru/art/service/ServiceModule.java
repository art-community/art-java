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

package ru.art.service;

import lombok.Getter;
import ru.art.core.module.Module;
import ru.art.service.state.ServiceModuleState;
import static org.apache.logging.log4j.ThreadContext.put;
import static ru.art.core.context.Context.context;
import static ru.art.logging.LoggingModuleConstants.LoggingParameters.REQUEST_ID_KEY;
import static ru.art.service.constants.ServiceModuleConstants.DEFAULT_REQUEST_ID;
import static ru.art.service.constants.ServiceModuleConstants.SERVICE_MODULE_ID;

@Getter
public class ServiceModule implements Module<ServiceModuleConfiguration, ServiceModuleState> {
    private final ServiceModuleConfiguration defaultConfiguration = new ServiceModuleConfiguration.ServiceModuleDefaultConfiguration();
    private final ServiceModuleState state = new ServiceModuleState();
    private final String id = SERVICE_MODULE_ID;

    public static ServiceModuleConfiguration serviceModule() {
        return context().getModule(SERVICE_MODULE_ID, ServiceModule::new);
    }

    public static ServiceModuleState serviceModuleState() {
        return context().getModuleState(SERVICE_MODULE_ID, ServiceModule::new);
    }

    @Override
    public void onLoad() {
        put(REQUEST_ID_KEY, DEFAULT_REQUEST_ID);
    }
}
