package ru.adk.example.module;

import lombok.Getter;
import ru.adk.core.module.Module;
import ru.adk.example.api.communication.http.ExampleServiceHttpCommunicationSpecification;
import ru.adk.example.api.communication.grpc.ExampleServiceGrpcCommunicationSpecification;
import ru.adk.example.configuration.ExampleModuleConfiguration;
import ru.adk.example.configuration.ExampleModuleConfiguration.ExampleModuleDefaultConfiguration;
import ru.adk.example.specification.ExampleServiceSpecification;
import ru.adk.example.state.ExampleModuleState;
import ru.adk.metrics.http.specification.MetricServiceSpecification;
import ru.adk.soap.server.specification.SoapServiceExecutionSpecification;
import static lombok.AccessLevel.PRIVATE;
import static ru.adk.config.extensions.activator.AgileConfigurationsActivator.useAgileConfigurations;
import static ru.adk.core.context.Context.context;
import static ru.adk.core.extension.ThreadExtensions.thread;
import static ru.adk.example.constants.ExampleAppModuleConstants.EXAMPLE_MODULE_ID;
import static ru.adk.example.constants.ExampleAppModuleConstants.HTTP_SERVER_BOOTSTRAP_THREAD;
import static ru.adk.grpc.server.GrpcServer.grpcServer;
import static ru.adk.http.server.HttpServer.httpServer;
import static ru.adk.service.ServiceModule.serviceModule;

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