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

package io.art.model.module;

import io.art.model.communicator.*;
import io.art.model.configurator.*;
import io.art.model.consumer.*;
import io.art.model.producer.*;
import io.art.model.server.*;
import io.art.model.storage.*;
import lombok.*;
import static io.art.model.constants.ModelConstants.*;
import static lombok.AccessLevel.PRIVATE;
import java.util.function.*;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class ModuleModel {
    private final String mainModuleId;
    private final CommunicatorModel communicatorModel = new CommunicatorModel();
    private final ConfiguratorModel configuratorModel = new ConfiguratorModel();
    private final ConsumerModel consumerModel = new ConsumerModel();
    private final ProducerModel producerModel = new ProducerModel();
    private final ServerModel serverModel = new ServerModel();
    private final StorageModel storageModel = new StorageModel();

    public ModuleModel configure(UnaryOperator<ConfiguratorModel> configurator) {
        configurator.apply(configuratorModel);
        return this;
    }

    public ModuleModel serve(UnaryOperator<ServerModel> server) {
        server.apply(serverModel);
        return this;
    }

    public ModuleModel communicate(UnaryOperator<CommunicatorModel> communicator) {
        communicator.apply(communicatorModel);
        return this;
    }

    public ModuleModel store(UnaryOperator<StorageModel> storage) {
        storage.apply(storageModel);
        return this;
    }

    public ModuleModel consume(UnaryOperator<ConsumerModel> consumer) {
        consumer.apply(consumerModel);
        return this;
    }

    public ModuleModel produce(UnaryOperator<ProducerModel> producer) {
        producer.apply(producerModel);
        return this;
    }

    public static ModuleModel module() {
        return new ModuleModel(DEFAULT_MODULE_ID);
    }

    public static ModuleModel module(String id) {
        return new ModuleModel(id);
    }
}
