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

package io.art.scheduler.module;

import io.art.core.context.*;
import io.art.core.module.*;
import io.art.scheduler.executor.deferred.*;
import io.art.scheduler.executor.periodic.*;
import lombok.*;
import static io.art.core.context.Context.*;
import static io.art.scheduler.constants.SchedulerModuleConstants.*;
import static io.art.scheduler.factory.TaskFactory.*;
import static io.art.scheduler.manager.SchedulersManager.*;
import static io.art.scheduler.module.SchedulerModuleConfiguration.*;
import static java.time.LocalDateTime.*;
import static java.util.Objects.*;
import static lombok.AccessLevel.*;
import java.time.*;

@Getter
public class SchedulerModule implements StatelessModule<SchedulerModuleConfiguration, Configurator> {
    private final String id = SchedulerModule.class.getSimpleName();
    private final SchedulerModuleConfiguration configuration = new SchedulerModuleConfiguration();
    private final Configurator configurator = new Configurator(configuration);
    @Getter(lazy = true, value = PRIVATE)
    private static final StatelessModuleProxy<SchedulerModuleConfiguration> schedulerModule = context().getStatelessModule(SchedulerModule.class.getSimpleName());

    public static StatelessModuleProxy<SchedulerModuleConfiguration> schedulerModule() {
        return getSchedulerModule();
    }

    public static DeferredExecutor deferredExecutor() {
        return schedulerModule().configuration().getDeferredExecutor();
    }


    public static PeriodicExecutor periodicExecutor() {
        return schedulerModule().configuration().getPeriodicExecutor();
    }

    @Override
    public void onLoad(Context.Service contextService) {
        Duration duration = configuration.getRefreshDuration();
        if (isNull(duration)) return;
        scheduleDelayed(task(REFRESHER_TASK, contextService::reload), now().plus(duration), duration);
    }

    @Override
    public void beforeReload(Context.Service contextService) {
        periodicExecutor().cancelPeriodicTask(REFRESHER_TASK);
    }

    @Override
    public void afterReload(Context.Service contextService) {
        Duration duration = configuration.getRefreshDuration();
        if (isNull(duration)) return;
        scheduleDelayed(task(REFRESHER_TASK, contextService::reload), now().plus(duration), duration);
    }

    @Override
    public void onUnload(Context.Service contextService) {
        configuration.getDeferredExecutor().shutdown();
        configuration.getPeriodicExecutor().shutdown();
    }
}
