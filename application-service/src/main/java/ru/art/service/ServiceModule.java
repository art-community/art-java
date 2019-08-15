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

package ru.art.service;

import lombok.Getter;
import ru.art.core.module.Module;
import ru.art.service.state.ServiceModuleState;
import static lombok.AccessLevel.PRIVATE;
import static org.apache.logging.log4j.ThreadContext.put;
import static ru.art.core.context.Context.context;
import static ru.art.core.context.Context.insideDefaultContext;
import static ru.art.logging.LoggingModuleConstants.LoggingParameters.REQUEST_ID_KEY;
import static ru.art.service.constants.ServiceModuleConstants.DEFAULT_REQUEST_ID;
import static ru.art.service.constants.ServiceModuleConstants.SERVICE_MODULE_ID;

@Getter
public class ServiceModule implements Module<ServiceModuleConfiguration, ServiceModuleState> {
    @Getter(lazy = true, value = PRIVATE)
    private static final ServiceModuleConfiguration serviceModule = context().getModule(SERVICE_MODULE_ID, ServiceModule::new);
    @Getter(lazy = true, value = PRIVATE)
    private static final ServiceModuleState serviceModuleState = context().getModuleState(SERVICE_MODULE_ID, ServiceModule::new);
    private final String id = SERVICE_MODULE_ID;
    private final ServiceModuleConfiguration defaultConfiguration = ServiceModuleConfiguration.ServiceModuleDefaultConfiguration.DEFAULT_CONFIGURATION;
    private final ServiceModuleState state = new ServiceModuleState();

    public static ServiceModuleConfiguration serviceModule() {
        if (insideDefaultContext()) {
            return ServiceModuleConfiguration.DEFAULT_CONFIGURATION;
        }
        return getServiceModule();
    }

    public static ServiceModuleState serviceModuleState() {
        return getServiceModuleState();
    }

    @Override
    public void onLoad() {
        put(REQUEST_ID_KEY, DEFAULT_REQUEST_ID);
    }
}
