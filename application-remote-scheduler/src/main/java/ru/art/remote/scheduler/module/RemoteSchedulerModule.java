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

package ru.art.remote.scheduler.module;

import lombok.Getter;
import ru.art.core.module.Module;
import ru.art.remote.scheduler.configuration.RemoteSchedulerModuleConfiguration;
import ru.art.remote.scheduler.state.RemoteSchedulerModuleState;
import static lombok.AccessLevel.PRIVATE;
import static ru.art.core.context.Context.context;
import static ru.art.remote.scheduler.constants.RemoteSchedulerModuleConstants.REMOTE_SCHEDULER_MODULE_ID;
import static ru.art.remote.scheduler.controller.PoolController.fillAllPools;
import static ru.art.remote.scheduler.controller.PoolController.startPoolRefreshingTask;

@Getter
public class RemoteSchedulerModule implements Module<RemoteSchedulerModuleConfiguration, RemoteSchedulerModuleState> {
    @Getter(lazy = true, value = PRIVATE)
    private static final RemoteSchedulerModuleConfiguration remoteSchedulerModule = context().getModule(REMOTE_SCHEDULER_MODULE_ID, new RemoteSchedulerModule());
    @Getter(lazy = true, value = PRIVATE)
    private static final RemoteSchedulerModuleState remoteSchedulerModuleState = context().getModuleState(REMOTE_SCHEDULER_MODULE_ID, new RemoteSchedulerModule());
    private final RemoteSchedulerModuleConfiguration defaultConfiguration = new RemoteSchedulerModuleConfiguration();
    private final String id = REMOTE_SCHEDULER_MODULE_ID;
    private RemoteSchedulerModuleState state = new RemoteSchedulerModuleState();

    public static RemoteSchedulerModuleConfiguration remoteSchedulerModule() {
        return getRemoteSchedulerModule();
    }

    public static RemoteSchedulerModuleState remoteSchedulerModuleState() {
        return getRemoteSchedulerModuleState();
    }

    public static void startRemoteScheduler() {
        fillAllPools();
        startPoolRefreshingTask();
    }
}
