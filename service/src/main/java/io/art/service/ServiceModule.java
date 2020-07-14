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

package io.art.service;

import lombok.*;
import io.art.core.module.Module;
import io.art.service.state.*;
import static lombok.AccessLevel.*;
import static org.apache.logging.log4j.ThreadContext.*;
import static io.art.core.context.Context.*;
import static io.art.logging.LoggingModuleConstants.LoggingParameters.*;
import static io.art.service.ServiceModuleConfiguration.*;
import static io.art.service.constants.ServiceModuleConstants.*;

@Getter
public class ServiceModule implements Module<ServiceModuleConfiguration, ServiceModuleState> {
    @Getter(lazy = true, value = PRIVATE)
    private static final ServiceModuleConfiguration serviceModule = context().getModule(SERVICE_MODULE_ID, ServiceModule::new);
    @Getter(lazy = true, value = PRIVATE)
    private static final ServiceModuleState serviceModuleState = context().getModuleState(SERVICE_MODULE_ID, ServiceModule::new);
    private final String id = SERVICE_MODULE_ID;
    private final ServiceModuleConfiguration defaultConfiguration = DEFAULT_CONFIGURATION;
    private final ServiceModuleState state = new ServiceModuleState();

    public static ServiceModuleConfiguration serviceModule() {
        if (contextIsNotReady()) {
            return DEFAULT_CONFIGURATION;
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
