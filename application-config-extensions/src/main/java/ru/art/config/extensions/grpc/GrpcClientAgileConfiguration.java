package ru.art.config.extensions.grpc;

import lombok.Getter;
import ru.art.grpc.client.configuration.GrpcClientModuleConfiguration.GrpcClientModuleDefaultConfiguration;
import ru.art.grpc.client.model.GrpcCommunicationTargetConfiguration;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static java.util.stream.Collectors.toMap;
import static ru.art.config.extensions.ConfigExtensions.*;
import static ru.art.config.extensions.common.CommonConfigKeys.*;
import static ru.art.config.extensions.grpc.GrpcConfigKeys.*;
import static ru.art.core.checker.CheckerForEmptiness.ifEmpty;
import static ru.art.core.constants.StringConstants.SLASH;
import static ru.art.core.constants.ThreadConstants.DEFAULT_THREAD_POOL_SIZE;
import static ru.art.core.extension.ExceptionExtensions.ifException;
import static ru.art.core.extension.NullCheckingExtensions.getOrElse;
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
