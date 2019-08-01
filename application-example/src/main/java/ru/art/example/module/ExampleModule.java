package ru.art.example.module;

import lombok.Getter;
import ru.art.core.module.Module;
import ru.art.example.api.communication.http.ExampleServiceHttpCommunicationSpecification;
import ru.art.example.api.communication.grpc.ExampleServiceGrpcCommunicationSpecification;
import ru.art.example.configuration.ExampleModuleConfiguration;
import ru.art.example.configuration.ExampleModuleConfiguration.ExampleModuleDefaultConfiguration;
import ru.art.example.specification.ExampleServiceSpecification;
import ru.art.example.state.ExampleModuleState;
import ru.art.metrics.http.specification.MetricServiceSpecification;
import ru.art.soap.server.specification.SoapServiceExecutionSpecification;
import static lombok.AccessLevel.PRIVATE;
import static ru.art.config.extensions.activator.AgileConfigurationsActivator.useAgileConfigurations;
import static ru.art.core.context.Context.context;
import static ru.art.core.extension.ThreadExtensions.thread;
import static ru.art.example.constants.ExampleAppModuleConstants.EXAMPLE_MODULE_ID;
import static ru.art.example.constants.ExampleAppModuleConstants.HTTP_SERVER_BOOTSTRAP_THREAD;
import static ru.art.grpc.server.GrpcServer.grpcServer;
import static ru.art.http.server.HttpServer.httpServer;
import static ru.art.service.ServiceModule.serviceModule;

/**
 * Module class is a main class where all needed modules are loading
 * and services are registering
 */

@Getter
public class ExampleModule implements Module<ExampleModuleConfiguration, ExampleModuleState> {
    @Getter(lazy = true, value = PRIVATE)
    private static final ExampleModuleConfiguration exampleModule = context().getModule(EXAMPLE_MODULE_ID, new ExampleModule());
    @Getter(lazy = true, value = PRIVATE)
    private static final ExampleModuleState exampleState = context().getModuleState(EXAMPLE_MODULE_ID, new ExampleModule());
    private final ExampleModuleState state = new ExampleModuleState();

    private final ExampleModuleConfiguration defaultConfiguration = new ExampleModuleDefaultConfiguration();
    private final String id = EXAMPLE_MODULE_ID;

    public static ExampleModuleConfiguration exampleModule() {
        return getExampleModule();
    }

    public static ExampleModuleState exampleState() {
        return getExampleState();
    }

    public static void main(String[] args) {
        startExample();
    }

    public static void startExample() {
        useAgileConfigurations(EXAMPLE_MODULE_ID);
        serviceModule().getServiceRegistry()
                .registerService(new SoapServiceExecutionSpecification(new ExampleServiceSpecification()))
                .registerService(new ExampleServiceSpecification())
                .registerService(new MetricServiceSpecification())
                .registerService(new ExampleServiceGrpcCommunicationSpecification())
                .registerService(new ExampleServiceHttpCommunicationSpecification());
        thread(HTTP_SERVER_BOOTSTRAP_THREAD, () -> httpServer().await());
        grpcServer().await();
    }
}