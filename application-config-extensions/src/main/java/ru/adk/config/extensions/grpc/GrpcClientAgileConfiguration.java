package ru.adk.config.extensions.grpc;

import lombok.Getter;
import ru.adk.grpc.client.configuration.GrpcClientModuleConfiguration.GrpcClientModuleDefaultConfiguration;
import ru.adk.grpc.client.model.GrpcCommunicationTargetConfiguration;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static java.util.stream.Collectors.toMap;
import static ru.adk.config.extensions.ConfigExtensions.*;
import static ru.adk.config.extensions.common.CommonConfigKeys.*;
import static ru.adk.config.extensions.grpc.GrpcConfigKeys.*;
import static ru.adk.core.checker.CheckerForEmptiness.ifEmpty;
import static ru.adk.core.constants.StringConstants.SLASH;
import static ru.adk.core.constants.ThreadConstants.DEFAULT_THREAD_POOL_SIZE;
import static ru.adk.core.extension.ExceptionExtensions.ifException;
import static ru.adk.core.extension.NullCheckingExtensions.getOrElse;
import java.util.Map;
import java.util.concurrent.Executor;


@Getter
public class GrpcClientAgileConfiguration extends GrpcClientModuleDefaultConfiguration {
    private long timeout;
    private Executor overridingExecutor;
    private String balancerHost;
    private int balancerPort;
    private Map<String, GrpcCommunicationTargetConfiguration> communicationTargets;

    public GrpcClientAgileConfiguration() {
        refresh();
    }

    @Override
    public void refresh() {
        timeout = configLong(GRPC_COMMUNICATION_SECTION_ID, TIMEOUT_MILLIS, super.getTimeout());
        overridingExecutor = newFixedThreadPool(configInt(GRPC_COMMUNICATION_SECTION_ID, THREAD_POOL_SIZE, DEFAULT_THREAD_POOL_SIZE));
        balancerHost = configString(GRPC_BALANCER_SECTION_ID, HOST, super.getBalancerHost());
        balancerPort = configInt(GRPC_BALANCER_SECTION_ID, PORT, super.getBalancerPort());
        communicationTargets = ifException(() -> configMap(GRPC_COMMUNICATION_SECTION_ID, TARGETS).entrySet().stream().collect(toMap(Map.Entry::getKey, entry -> GrpcCommunicationTargetConfiguration.builder()
                .serviceId(entry.getKey())
                .host(ifEmpty(entry.getValue().getString(HOST), balancerHost))
                .port(getOrElse(entry.getValue().getInt(PORT), balancerPort))
                .path(getOrElse(entry.getValue().getString(PATH), SLASH))
                .secured(getOrElse(entry.getValue().getBool(SECURED), false))
                .timeout(getOrElse(entry.getValue().getLong(TIMEOUT_MILLIS), super.getTimeout()))
                .url(entry.getValue().getString(URL))
                .build())), super.getCommunicationTargets());
    }
}
