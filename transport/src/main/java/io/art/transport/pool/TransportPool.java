package io.art.transport.pool;

import io.art.transport.configuration.*;
import reactor.netty.http.*;
import reactor.netty.resources.*;
import reactor.netty.tcp.*;
import reactor.netty.udp.*;
import static java.util.Objects.*;
import static reactor.netty.resources.ConnectionProvider.*;

public class TransportPool {
    private final ConnectionProvider provider;
    private final LoopResources loopResources;

    public TransportPool(TransportPoolConfiguration configuration) {
        String loopResourcesPrefix = configuration.getLoopResourcesPrefix();
        int workersCount = configuration.getWorkersCount();
        int selectorsCount = configuration.getSelectorsCount();
        this.loopResources = LoopResources.create(loopResourcesPrefix, workersCount, selectorsCount, true);

        ConnectionProvider.Builder builder = ConnectionProvider.builder(configuration.getConnectionProviderName());
        if (configuration.isDisposeInactive()) {
            builder.disposeInactivePoolsInBackground(configuration.getDisposeInterval(), configuration.getPoolInactivity());
        }
        if (configuration.isEviction()) {
            builder.evictInBackground(configuration.getEvictionInterval());
        }
        if (LEASING_STRATEGY_FIFO.equals(configuration.getLeasingStrategy())) {
            builder.fifo();
        }
        if (LEASING_STRATEGY_LIFO.equals(configuration.getLeasingStrategy())) {
            builder.lifo();
        }
        if (configuration.isMetrics()) {
            builder.metrics(true);
            if (nonNull(configuration.getMeterRegistrar())) {
                builder.metrics(configuration.isMetrics(), configuration.getMeterRegistrar());
            }
        }
        builder
                .pendingAcquireMaxCount(configuration.getPendingAcquireMaxCount())
                .pendingAcquireTimeout(configuration.getPendingAcquireTimeout())
                .maxConnections(configuration.getMaxConnections())
                .maxIdleTime(configuration.getMaxIdleTime())
                .maxLifeTime(configuration.getMaxLifeTime());
        this.provider = builder.build();
    }

    public static void configureCommonTransportPool(TransportPoolConfiguration configuration) {
        TransportPool transportPool = new TransportPool(configuration);
        TcpResources.set(transportPool.provider);
        TcpResources.set(transportPool.loopResources);
        UdpResources.set(transportPool.loopResources);
        HttpResources.set(transportPool.provider);
        HttpResources.set(transportPool.loopResources);
    }

    public static void shutdownCommonTransportPool() {
        TcpResources.get().dispose();
        HttpResources.get().dispose();
        UdpResources.get().dispose();
    }
}
