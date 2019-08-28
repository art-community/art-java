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

package ru.art.config.extensions.grpc;

import lombok.*;
import ru.art.grpc.client.configuration.GrpcClientModuleConfiguration.*;
import ru.art.grpc.client.model.*;
import static java.util.stream.Collectors.*;
import static ru.art.config.extensions.ConfigExtensions.*;
import static ru.art.config.extensions.common.CommonConfigKeys.*;
import static ru.art.config.extensions.grpc.GrpcConfigKeys.*;
import static ru.art.core.checker.CheckerForEmptiness.*;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.core.constants.ThreadConstants.*;
import static ru.art.core.extension.ExceptionExtensions.*;
import static ru.art.core.extension.NullCheckingExtensions.*;
import static ru.art.grpc.client.model.GrpcCommunicationTargetConfiguration.grpcCommunicationTarget;
import java.util.*;
import java.util.concurrent.*;


@Getter
public class GrpcClientAgileConfiguration extends GrpcClientModuleDefaultConfiguration {
    private long timeout;
    private Executor overridingExecutor;
    private String balancerHost;
    private int balancerPort;
    private Map<String, GrpcCommunicationTargetConfiguration> communicationTargets;
    private boolean enableRawDataTracing;
    private boolean enableValueTracing;

    public GrpcClientAgileConfiguration() {
        refresh();
    }

    @Override
    public void refresh() {
        enableRawDataTracing = configBoolean(GRPC_COMMUNICATION_SECTION_ID, ENABLE_RAW_DATA_TRACING, super.isEnableRawDataTracing());
        enableValueTracing = configBoolean(GRPC_COMMUNICATION_SECTION_ID, ENABLE_VALUE_TRACING, super.isEnableValueTracing());
        timeout = configLong(GRPC_COMMUNICATION_SECTION_ID, TIMEOUT, super.getTimeout());
        overridingExecutor = new ForkJoinPool(configInt(GRPC_COMMUNICATION_SECTION_ID, THREAD_POOL_SIZE, DEFAULT_THREAD_POOL_SIZE));
        balancerHost = configString(GRPC_BALANCER_SECTION_ID, HOST, super.getBalancerHost());
        balancerPort = configInt(GRPC_BALANCER_SECTION_ID, PORT, super.getBalancerPort());
        communicationTargets = ifException(() -> configMap(GRPC_COMMUNICATION_SECTION_ID, TARGETS).entrySet().stream().collect(toMap(Map.Entry::getKey, entry -> grpcCommunicationTarget()
                .host(ifEmpty(entry.getValue().getString(HOST), balancerHost))
                .port(getOrElse(entry.getValue().getInt(PORT), balancerPort))
                .path(getOrElse(entry.getValue().getString(PATH), SLASH))
                .secured(getOrElse(entry.getValue().getBool(SECURED), false))
                .timeout(getOrElse(entry.getValue().getLong(TIMEOUT), super.getTimeout()))
                .url(entry.getValue().getString(URL))
                .build())), super.getCommunicationTargets());
    }
}
