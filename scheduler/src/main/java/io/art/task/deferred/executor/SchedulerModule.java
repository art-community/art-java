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

package io.art.task.deferred.executor;

import io.art.core.module.*;
import lombok.*;
import static io.art.core.context.Context.*;
import static io.art.task.deferred.executor.SchedulerModuleConfiguration.*;
import static lombok.AccessLevel.*;

@Getter
public class SchedulerModule implements StatelessModule<SchedulerModuleConfiguration, Configurator> {
    private final String id = SchedulerModule.class.getSimpleName();
    private final SchedulerModuleConfiguration configuration = new SchedulerModuleConfiguration();
    private final Configurator configurator = new Configurator(configuration);
    @Getter(lazy = true, value = PRIVATE)
    private final static StatelessModuleProxy<SchedulerModuleConfiguration> schedulerModule = context().getStatelessModule(StatelessModule.class.getSimpleName());

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
    public void onUnload() {
        DeferredExecutor deferredExecutor = deferredExecutor();
        PeriodicExecutor periodicExecutor = periodicExecutor();
        deferredExecutor.clear();
        deferredExecutor.shutdown();
        periodicExecutor.clear();
        periodicExecutor.shutdown();
    }

}
