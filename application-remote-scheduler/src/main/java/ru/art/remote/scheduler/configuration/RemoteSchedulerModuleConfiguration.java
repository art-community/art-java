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

package ru.art.remote.scheduler.configuration;

import lombok.*;
import ru.art.core.module.*;

import static ru.art.config.extensions.ConfigExtensions.*;
import static ru.art.core.constants.NetworkConstants.*;
import static ru.art.http.constants.HttpCommonConstants.*;
import static ru.art.remote.scheduler.constants.RemoteSchedulerModuleConstants.Defaults.*;
import static ru.art.remote.scheduler.constants.RemoteSchedulerModuleConstants.SchedulerConfigKeys.*;

@Getter
public class RemoteSchedulerModuleConfiguration implements ModuleConfiguration {
    private String balancerHost;
    private int balancerPort;
    private String dbAdapterServiceId;
    private int refreshDeferredPeriodMinutes;
    private int refreshPeriodicPeriodMinutes;

    public RemoteSchedulerModuleConfiguration() {
        refresh();
    }

    @Override
    public void refresh() {
        balancerHost = configString(BALANCER_SECTION_ID, BALANCER_HOST, LOCALHOST);
        balancerPort = configInt(BALANCER_SECTION_ID, BALANCER_PORT, DEFAULT_HTTP_PORT);
        dbAdapterServiceId = configString(DB_ADAPTER_SECTION_ID, SERVICE_ID, DEFAULT_DB_ADAPTER_SERVICE_ID);
        refreshDeferredPeriodMinutes = configInt(SCHEDULER_SECTION_ID, REFRESH_DEFERRED_PERIOD_MINUTES, DEFAULT_REFRESH_DEFERRED_PERIOD_MINUTES);
        refreshPeriodicPeriodMinutes = configInt(SCHEDULER_SECTION_ID, REFRESH_PERIODIC_PERIOD_MINUTES, DEFAULT_REFRESH_PERIODIC_PERIOD_MINUTES);
    }
}
