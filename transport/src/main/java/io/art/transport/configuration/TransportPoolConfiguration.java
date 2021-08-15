package io.art.transport.configuration;

import lombok.*;
import reactor.netty.resources.*;
import static io.art.transport.constants.TransportModuleConstants.Defaults.*;
import java.time.*;
import java.util.function.*;

@Getter
@Builder
public class TransportPoolConfiguration {
    private final Duration pendingAcquireTimeout;
    private final int maxConnections;
    private final int pendingAcquireMaxCount;
    private final Duration maxIdleTime;
    private final Duration maxLifeTime;
    private final boolean metrics;
    private final Supplier<? extends ConnectionProvider.MeterRegistrar> meterRegistrar;
    private final String leasingStrategy;
    private final boolean eviction;
    private final Duration evictionInterval;
    private final int workersCount;
    private final int selectorsCount;
    private final String connectionProviderName;
    private final String loopResourcesPrefix;
    private final boolean disposeInactive;
    private final Duration disposeInterval;
    private final Duration poolInactivity;

    public static TransportPoolConfiguration defaults() {
        return TransportPoolConfiguration.builder()
                .maxConnections(DEFAULT_MAX_CONNECTIONS)
                .maxIdleTime(DEFAULT_MAX_IDLE_TIME)
                .maxLifeTime(DEFAULT_MAX_LIFE_TIME)
                .pendingAcquireTimeout(DEFAULT_PENDING_ACQUIRE_TIMEOUT)
                .pendingAcquireMaxCount(DEFAULT_PENDING_ACQUIRE_MAX_COUNT)
                .leasingStrategy(DEFAULT_LEASING_STRATEGY)
                .evictionInterval(DEFAULT_EVICTION_INTERVAL)
                .workersCount(DEFAULT_WORKERS_COUNT)
                .selectorsCount(DEFAULT_SELECTORS_COUNT)
                .connectionProviderName(DEFAULT_CONNECTION_PROVIDER_NAME)
                .loopResourcesPrefix(DEFAULT_LOOP_RESOURCES_PREFIX)
                .build();
    }
}
