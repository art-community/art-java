package ru.art.task.deferred.executor;

import lombok.Getter;
import ru.art.core.module.Module;
import ru.art.core.module.ModuleState;
import ru.art.task.deferred.executor.SchedulerModuleConfiguration.SchedulerModuleDefaultConfiguration;
import static lombok.AccessLevel.PRIVATE;
import static ru.art.core.context.Context.context;
import static ru.art.task.deferred.executor.SchedulerModuleConstants.SCHEDULER_MODULE_ID;

@Getter
public class SchedulerModule implements Module<SchedulerModuleConfiguration, ModuleState> {
    @Getter(lazy = true, value = PRIVATE)
    private final static SchedulerModuleConfiguration schedulerModule = context().getModule(SCHEDULER_MODULE_ID, new SchedulerModule());
    @Getter
    private final SchedulerModuleConfiguration defaultConfiguration = new SchedulerModuleDefaultConfiguration();
    private final String id = SCHEDULER_MODULE_ID;

    public static SchedulerModuleConfiguration schedulerModule() {
        return getSchedulerModule();
    }
}
