package io.art.transport.configuration;

import io.art.core.source.*;
import lombok.*;
import reactor.netty.resources.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.transport.constants.TransportModuleConstants.ConfigurationKeys.*;
import static io.art.transport.constants.TransportModuleConstants.Defaults.*;
import java.time.*;
import java.util.function.*;

@Getter
@Builder
@ToString
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

    public static TransportPoolConfiguration from(ConfigurationSource source, TransportPoolConfiguration current) {
        TransportPoolConfigurationBuilder builder = TransportPoolConfiguration.builder();
        builder.pendingAcquireTimeout = orElse(source.getDuration(PENDING_ACQUIRE_TIMEOUT_KEY), current.pendingAcquireTimeout);
        builder.maxConnections = orElse(source.getInteger(MAX_CONNECTIONS_KEY), current.maxConnections);
        builder.pendingAcquireMaxCount = orElse(source.getInteger(PENDING_ACQUIRE_MAX_COUNT_KEY), current.pendingAcquireMaxCount);
        builder.maxIdleTime = orElse(source.getDuration(MAX_IDLE_TIME_KEY), current.maxIdleTime);
        builder.maxLifeTime = orElse(source.getDuration(MAX_LIFE_TIME_KEY), current.maxLifeTime);
        builder.metrics = orElse(source.getBoolean(metrics_KEY), current.metrics);
        builder.leasingStrategy = orElse(source.getString(LEASING_STRATEGY_KEY), current.leasingStrategy);
        builder.eviction = orElse(source.getBoolean(EVICTION_KEY), current.eviction);
        builder.evictionInterval = orElse(source.getDuration(EVICTION_INTERVAL_KEY), current.evictionInterval);
        builder.workersCount = orElse(source.getInteger(WORKERS_COUNT_KEY), current.workersCount);
        builder.selectorsCount = orElse(source.getInteger(SELECTORS_COUNT_KEY), current.selectorsCount);
        builder.connectionProviderName = orElse(source.getString(CONNECTION_PROVIDER_NAME_KEY), current.connectionProviderName);
        builder.loopResourcesPrefix = orElse(source.getString(LOOP_RESOURCES_PREFIX_KEY), current.loopResourcesPrefix);
        builder.disposeInactive = orElse(source.getBoolean(DISPOSE_INACTIVE_KEY), current.disposeInactive);
        builder.disposeInterval = orElse(source.getDuration(DISPOSE_INTERVAL_KEY), current.disposeInterval);
        builder.poolInactivity = orElse(source.getDuration(POOL_INACTIVITY_KEY), current.poolInactivity);

        return builder.build();
    }

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
