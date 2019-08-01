package ru.adk.state.module;

import lombok.Getter;
import ru.adk.core.module.Module;
import ru.adk.http.server.specification.HttpWebUiServiceSpecification;
import ru.adk.metrics.http.specification.MetricServiceSpecification;
import ru.adk.state.ApplicationState;
import ru.adk.state.configuration.ApplicationStateModuleConfiguration;
import ru.adk.state.configuration.ApplicationStateModuleConfiguration.ApplicationStateModuleDefaultConfiguration;
import ru.adk.state.service.NetworkService;
import ru.adk.state.specification.LockServiceSpecification;
import ru.adk.state.specification.NetworkServiceSpecification;
import static java.time.Duration.ofSeconds;
import static ru.adk.config.extensions.activator.AgileConfigurationsActivator.useAgileConfigurations;
import static ru.adk.core.context.Context.context;
import static ru.adk.grpc.server.GrpcServer.grpcServer;
import static ru.adk.http.server.HttpServer.httpServerInSeparatedThread;
import static ru.adk.service.ServiceModule.serviceModule;
import static ru.adk.state.constants.StateModuleConstants.APPLICATION_STATE_MODULE_ID;
import static ru.adk.state.dao.ClusterDao.loadCluster;
import static ru.adk.task.deferred.executor.IdentifiedRunnableFactory.commonTask;
import static ru.adk.task.deferred.executor.SchedulerModuleActions.asynchronousPeriod;

@Getter
public class ApplicationStateModule implements Module<ApplicationStateModuleConfiguration, ApplicationState> {
    @Getter(lazy = true)
    private final static ApplicationStateModuleConfiguration applicationStateModule = context().getModule(APPLICATION_STATE_MODULE_ID, new ApplicationStateModule());
    @Getter(lazy = true)
    private final static ApplicationState applicationState = context().getModuleState(APPLICATION_STATE_MODULE_ID, new ApplicationStateModule());
    private final ApplicationState state = new ApplicationState();
    private final String id = APPLICATION_STATE_MODULE_ID;
    private final ApplicationStateModuleConfiguration defaultConfiguration = new ApplicationStateModuleDefaultConfiguration();

    public static ApplicationStateModuleConfiguration applicationStateModule() {
        return getApplicationStateModule();
    }

    public static ApplicationState applicationState() {
        return getApplicationState();
    }

    public static void main(String[] args) {
        startApplicationState();
    }

    @SuppressWarnings("WeakerAccess")
    public static void startApplicationState() {
        useAgileConfigurations(APPLICATION_STATE_MODULE_ID);
        serviceModule().getServiceRegistry()
                .registerService(new NetworkServiceSpecification())
                .registerService(new LockServiceSpecification())
                .registerService(new HttpWebUiServiceSpecification())
                .registerService(new MetricServiceSpecification());
        applicationState().setCluster(loadCluster());
        asynchronousPeriod(commonTask(NetworkService::removeDeadEndpoints), ofSeconds(applicationStateModule().getModuleEndpointCheckRateSeconds()));
        httpServerInSeparatedThread();
        grpcServer().await();
    }
}
