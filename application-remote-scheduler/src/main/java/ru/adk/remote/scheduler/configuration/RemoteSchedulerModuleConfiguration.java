package ru.adk.remote.scheduler.configuration;

import lombok.Getter;
import ru.adk.core.module.ModuleConfiguration;
import static ru.adk.config.extensions.ConfigExtensions.configInt;
import static ru.adk.config.extensions.ConfigExtensions.configString;
import static ru.adk.remote.scheduler.constants.RemoteSchedulerModuleConstants.SchedulerConfigKeys.*;

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
