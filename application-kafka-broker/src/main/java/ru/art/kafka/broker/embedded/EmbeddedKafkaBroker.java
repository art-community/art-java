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

package ru.art.kafka.broker.embedded;

import kafka.server.*;
import kafka.zk.AdminZkClient;
import kafka.zk.KafkaZkClient;
import lombok.*;
import ru.art.kafka.broker.configuration.*;
import scala.collection.immutable.List;
import static java.util.Objects.*;
import static kafka.server.KafkaConfig.*;
import static lombok.AccessLevel.*;
import static org.apache.kafka.common.utils.Time.*;
import static ru.art.core.constants.NetworkConstants.*;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.kafka.broker.constants.KafkaBrokerModuleConstants.*;
import static ru.art.kafka.broker.constants.KafkaBrokerModuleConstants.ZookeeperInitializationMode.*;
import static ru.art.kafka.broker.embedded.EmbeddedZookeeper.*;
import static ru.art.kafka.broker.module.KafkaBrokerModule.*;
import static scala.Option.*;
import java.util.*;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class EmbeddedKafkaBroker {
    private final KafkaBrokerConfiguration kafkaBrokerConfiguration;
    private final ZookeeperConfiguration zookeeperConfiguration;
    private final ZookeeperInitializationMode zookeeperInitializationMode;
    private final KafkaServer server;
    private EmbeddedZookeeper embeddedZookeeper;

    /**
     *
     * @param kafkaBrokerConfiguration - custom broker configuration to fill properties instance to start KafkaService;
     * @param zookeeperConfiguration - custom zookeeper configuration to start Zookeeper;
     * @param zookeeperInitializationMode - parameter allows different ways of initializing Zookeeper.
     * if is not equals ON_KAFKA_BROKER_INITIALIZATION value, Zookeeper is not started;
     * @return broker instance;
     */
    public static EmbeddedKafkaBroker startKafkaBroker(KafkaBrokerConfiguration kafkaBrokerConfiguration, ZookeeperConfiguration zookeeperConfiguration, ZookeeperInitializationMode zookeeperInitializationMode) {
        Properties properties = new Properties();
        properties.put(ZkConnectProp(), kafkaBrokerConfiguration.getZookeeperConnection());
        properties.put(ListenersProp(), PLAINTEXT + LOCALHOST_IP_ADDRESS + COLON + kafkaBrokerConfiguration.getPort());
        properties.put(LogDirProp(), kafkaBrokerConfiguration.getLogDirectory());
        properties.put(OffsetsTopicReplicationFactorProp(), kafkaBrokerConfiguration.getReplicationFactor());
        properties.putAll(kafkaBrokerConfiguration.getAdditionalProperties());
        KafkaServer kafkaServer = new KafkaServer(new KafkaConfig(properties), SYSTEM, empty(), List.empty());
        KafkaZkClient kafkaZkClient = kafkaServer.zkClient();
        AdminZkClient adminZkClient = new AdminZkClient(kafkaZkClient);
        //adminZkClient.createTopic();
        if (zookeeperInitializationMode == ON_KAFKA_BROKER_INITIALIZATION) {
            EmbeddedKafkaBroker broker = new EmbeddedKafkaBroker(kafkaBrokerConfiguration,
                    zookeeperConfiguration,
                    zookeeperInitializationMode,
                    kafkaServer,
                    startZookeeper(zookeeperConfiguration));
            kafkaBrokerModuleState().setBroker(broker);
            kafkaServer.startup();
            return broker;
        }
        EmbeddedKafkaBroker broker = new EmbeddedKafkaBroker(kafkaBrokerConfiguration, zookeeperConfiguration, zookeeperInitializationMode, kafkaServer);
        kafkaBrokerModuleState().setBroker(broker);
        kafkaServer.startup();
        return broker;
    }

    public static EmbeddedKafkaBroker startKafkaBroker(KafkaBrokerConfiguration configuration, ZookeeperInitializationMode zookeeperInitializationMode) {
        return startKafkaBroker(configuration, kafkaBrokerModule().getZookeeperConfiguration(), zookeeperInitializationMode);
    }


    public static EmbeddedKafkaBroker startKafkaBroker(KafkaBrokerConfiguration configuration) {
        return startKafkaBroker(configuration, kafkaBrokerModule().getZookeeperConfiguration(), kafkaBrokerModule().getZookeeperInitializationMode());
    }

    public static EmbeddedKafkaBroker startKafkaBroker() {
        return startKafkaBroker(kafkaBrokerModule().getKafkaBrokerConfiguration());
    }

    public void await() {
        server.awaitShutdown();
    }

    public void shutdown() {
        server.shutdown();
        server.awaitShutdown();
    }

    public void restart() {
        shutdown();
        startKafkaBroker(kafkaBrokerConfiguration, zookeeperConfiguration, MANUAL);
    }

    public void restartWithZookeeper() {
        shutdown();
        if (nonNull(embeddedZookeeper)) {
            embeddedZookeeper.restart();
        }
        startKafkaBroker(kafkaBrokerConfiguration, zookeeperConfiguration, MANUAL);
    }

    public boolean isWorking() {
        return server.brokerState().currentState() == RunningAsBroker.state();
    }
}
