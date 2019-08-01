package ru.adk.remote.scheduler.module;

import lombok.Getter;
import ru.adk.core.module.Module;
import ru.adk.remote.scheduler.configuration.RemoteSchedulerModuleConfiguration;
import ru.adk.remote.scheduler.state.RemoteSchedulerModuleState;
import static lombok.AccessLevel.PRIVATE;
import static ru.adk.core.context.Context.context;
import static ru.adk.remote.scheduler.constants.RemoteSchedulerModuleConstants.REMOTE_SCHEDULER_MODULE_ID;
import static ru.adk.remote.scheduler.controller.PoolController.fillAllPools;
import static ru.adk.remote.scheduler.controller.PoolController.startPoolRefreshingTask;

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
