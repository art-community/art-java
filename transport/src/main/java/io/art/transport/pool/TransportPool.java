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
        this.loopResources = LoopResources.create(loopResourcesPrefix, selectorsCount, workersCount, true);

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
        if (configuration.getMaxIdleTime().getNano() > 0) {
            builder.maxIdleTime(configuration.getMaxIdleTime());
        }
        if (configuration.getMaxLifeTime().getNano() > 0) {
            builder.maxLifeTime(configuration.getMaxLifeTime());
        }
        if (configuration.getPendingAcquireMaxCount() > 0) {
            builder.pendingAcquireMaxCount(configuration.getPendingAcquireMaxCount());
        }
        builder
                .pendingAcquireTimeout(configuration.getPendingAcquireTimeout())
                .maxConnections(configuration.getMaxConnections());
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
}
