package ru.art.state.module;

import lombok.Getter;
import ru.art.core.module.Module;
import ru.art.http.server.specification.HttpWebUiServiceSpecification;
import ru.art.metrics.http.specification.MetricServiceSpecification;
import ru.art.state.ApplicationState;
import ru.art.state.configuration.ApplicationStateModuleConfiguration;
import ru.art.state.configuration.ApplicationStateModuleConfiguration.ApplicationStateModuleDefaultConfiguration;
import ru.art.state.service.NetworkService;
import ru.art.state.specification.LockServiceSpecification;
import ru.art.state.specification.NetworkServiceSpecification;
import static java.time.Duration.ofSeconds;
import static ru.art.config.extensions.activator.AgileConfigurationsActivator.useAgileConfigurations;
import static ru.art.core.context.Context.context;
import static ru.art.grpc.server.GrpcServer.grpcServer;
import static ru.art.http.server.HttpServer.httpServerInSeparatedThread;
import static ru.art.service.ServiceModule.serviceModule;
import static ru.art.state.constants.StateModuleConstants.APPLICATION_STATE_MODULE_ID;
import static ru.art.state.dao.ClusterDao.loadCluster;
import static ru.art.task.deferred.executor.IdentifiedRunnableFactory.commonTask;
import static ru.art.task.deferred.executor.SchedulerModuleActions.asynchronousPeriod;

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
