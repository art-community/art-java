/*
 * ART Java
 *
 * Copyright 2019 ART
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

package ru.art.example.module;

import lombok.*;
import ru.art.core.module.*;
import ru.art.example.api.communication.grpc.*;
import ru.art.example.api.communication.http.*;
import ru.art.example.configuration.*;
import ru.art.example.configuration.ExampleModuleConfiguration.*;
import ru.art.example.specification.*;
import ru.art.example.state.*;
import ru.art.soap.server.specification.*;

import static lombok.AccessLevel.*;
import static ru.art.config.extensions.activator.AgileConfigurationsActivator.*;
import static ru.art.core.context.Context.*;
import static ru.art.core.extension.ThreadExtensions.*;
import static ru.art.example.constants.ExampleAppModuleConstants.*;
import static ru.art.grpc.server.GrpcServer.*;
import static ru.art.http.server.HttpServer.*;
import static ru.art.service.ServiceModule.*;

/**
 * Module class is a main class where all needed modules are loading
 * and services are registering
 */

@Getter
public class ExampleModule implements Module<ExampleModuleConfiguration, ExampleModuleState> {
    @Getter(lazy = true, value = PRIVATE)
    private static final ExampleModuleConfiguration exampleModule = context().getModule(EXAMPLE_MODULE_ID, ExampleModule::new);
    @Getter(lazy = true, value = PRIVATE)
    private static final ExampleModuleState exampleState = context().getModuleState(EXAMPLE_MODULE_ID, ExampleModule::new);
    private final String id = EXAMPLE_MODULE_ID;
    private final ExampleModuleConfiguration defaultConfiguration = new ExampleModuleDefaultConfiguration();
    private final ExampleModuleState state = new ExampleModuleState();

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
                .registerService(new ExampleServiceGrpcCommunicationSpecification())
                .registerService(new ExampleServiceHttpCommunicationSpecification());
        startHttpServer();
        startGrpcServer().await();
    }
}