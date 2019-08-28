/*
 *    Copyright 2019 ART
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package ru.art.kafka.broker.configuration;

import lombok.*;
import ru.art.core.module.*;
import static ru.art.kafka.broker.constants.KafkaBrokerModuleConstants.*;
import static ru.art.kafka.broker.constants.KafkaBrokerModuleConstants.ZookeeperInitializationMode.*;

public interface KafkaBrokerModuleConfiguration extends ModuleConfiguration {
    ZookeeperConfiguration getZookeeperConfiguration();

    ZookeeperInitializationMode getZookeeperInitializationMode();

    KafkaBrokerConfiguration getKafkaBrokerConfiguration();

    KafkaBrokerModuleDefaultConfiguration DEFAULT_CONFIGURATION = new KafkaBrokerModuleDefaultConfiguration();

    @Getter
    class KafkaBrokerModuleDefaultConfiguration implements KafkaBrokerModuleConfiguration {
        private final ZookeeperConfiguration zookeeperConfiguration = ZookeeperConfiguration.builder().build();
        private final ZookeeperInitializationMode zookeeperInitializationMode = ON_KAFKA_BROKER_INITIALIZATION;
        private final KafkaBrokerConfiguration kafkaBrokerConfiguration = KafkaBrokerConfiguration.builder().build();
    }
}
