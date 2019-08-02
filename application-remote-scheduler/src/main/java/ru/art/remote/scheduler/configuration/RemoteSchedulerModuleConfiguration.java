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

package ru.art.remote.scheduler.configuration;

import lombok.Getter;
import ru.art.core.module.ModuleConfiguration;
import static ru.art.config.extensions.ConfigExtensions.configInt;
import static ru.art.config.extensions.ConfigExtensions.configString;
import static ru.art.remote.scheduler.constants.RemoteSchedulerModuleConstants.SchedulerConfigKeys.*;

@Getter
public class RemoteSchedulerModuleConfiguration implements ModuleConfiguration {
    private String balancerHost = configString(BALANCER_SECTION_ID, BALANCER_HOST);
    private int balancerPort = configInt(BALANCER_SECTION_ID, BALANCER_PORT);
    private String dbAdapterServiceId = configString(DB_ADAPTER_SECTION_ID, SERVICE_ID);
    private int refreshDeferredPeriodMinutes = configInt(SCHEDULER_SECTION_ID, REFRESH_DEFERRED_PERIOD_MINUTES);
    private int refreshPeriodicPeriodMinutes = configInt(SCHEDULER_SECTION_ID, REFRESH_PERIODIC_PERIOD_MINUTES);
    private String httpPath = configString(SCHEDULER_SECTION_ID, HTTP_PATH_KEY);

    @Override
    public void refresh() {
        balancerHost = configString(BALANCER_SECTION_ID, BALANCER_HOST);
        balancerPort = configInt(BALANCER_SECTION_ID, BALANCER_PORT);
        dbAdapterServiceId = configString(DB_ADAPTER_SECTION_ID, SERVICE_ID);
        refreshDeferredPeriodMinutes = configInt(SCHEDULER_SECTION_ID, REFRESH_DEFERRED_PERIOD_MINUTES);
        refreshPeriodicPeriodMinutes = configInt(SCHEDULER_SECTION_ID, REFRESH_PERIODIC_PERIOD_MINUTES);
        httpPath = configString(SCHEDULER_SECTION_ID, HTTP_PATH_KEY);
    }
}
