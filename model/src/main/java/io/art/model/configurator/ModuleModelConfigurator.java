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

package io.art.model.configurator;

import io.art.model.implementation.module.*;
import lombok.*;
import static io.art.model.constants.ModelConstants.*;
import java.util.function.*;

@Getter
@RequiredArgsConstructor
public class ModuleModelConfigurator {
    private final String moduleId;
    private final ConfiguratorModelConfigurator configurator = new ConfiguratorModelConfigurator();
    private final ServerModelConfigurator server = new ServerModelConfigurator();
    private final CommunicatorModelConfigurator communicator = new CommunicatorModelConfigurator();
    private Runnable onLoad = () -> {
    };

    public ModuleModelConfigurator serve(UnaryOperator<ServerModelConfigurator> server) {
        server.apply(this.server);
        return this;
    }

    public ModuleModelConfigurator communicate(UnaryOperator<CommunicatorModelConfigurator> communicator) {
        communicator.apply(this.communicator);
        return this;
    }

    public ModuleModelConfigurator configure(UnaryOperator<ConfiguratorModelConfigurator> configurator) {
        configurator.apply(this.configurator);
        return this;
    }

    public ModuleModelConfigurator onLoad(Runnable action) {
        this.onLoad = action;
        return this;
    }

    public ModuleModel configure() {
        return ModuleModel.builder()
                .mainModuleId(moduleId)
                .configuratorModel(configurator.configure())
                .serverModel(server.configure())
                .communicatorModel(communicator.configure())
                .onLoad(onLoad)
                .build();
    }

    public static ModuleModelConfigurator module(String id) {
        return new ModuleModelConfigurator(id);
    }

    public static ModuleModelConfigurator module() {
        return module(DEFAULT_MODULE_ID);
    }

    public static ModuleModelConfigurator module(Class<?> mainClass) {
        return module(mainClass.getSimpleName());
    }
}
