package io.art.transport.constants;

import lombok.*;
import static io.art.core.constants.ThreadConstants.*;
import static io.art.transport.constants.TransportModuleConstants.BufferType.*;
import static java.lang.Integer.*;
import static java.lang.Math.max;
import static java.lang.Math.*;
import static java.time.Duration.*;
import static reactor.netty.resources.ConnectionProvider.*;
import java.time.*;

public interface TransportModuleConstants {
    String TRANSPORT_LOGGER = "transport";

    interface Defaults {
        String DEFAULT_LOOP_RESOURCES_PREFIX = "common-transport";
        String DEFAULT_CONNECTION_PROVIDER_NAME = "common-connection";
        int DEFAULT_SELECTORS_COUNT = 2;
        int DEFAULT_WORKERS_COUNT = (int) max(ceil(DEFAULT_THREAD_POOL_SIZE >> 1), 2);
        Duration DEFAULT_EVICTION_INTERVAL = ZERO;
        int DEFAULT_MAX_CONNECTIONS = 1024;
        int DEFAULT_PENDING_ACQUIRE_MAX_COUNT = -2;
        Duration DEFAULT_PENDING_ACQUIRE_TIMEOUT = ofSeconds(30);
        Duration DEFAULT_MAX_LIFE_TIME = ofMillis(-1);
        Duration DEFAULT_MAX_IDLE_TIME = ofMillis(-1);
        String DEFAULT_LEASING_STRATEGY = LEASING_STRATEGY_FIFO;
        BufferType DEFAULT_BUFFER_TYPE = IO;
        int DEFAULT_BUFFER_INITIAL_CAPACITY = 256;
        int DEFAULT_BUFFER_MAX_CAPACITY = MAX_VALUE;
    }

    interface Messages {
        String TRANSPORT_CONFIGURING_MESSAGE = "Common transport resources configured by transport module";
    }

    interface ConfigurationKeys {
        String TRANSPORT_COMMON_SECTION = "transport.common";
        String PENDING_ACQUIRE_TIMEOUT_KEY = "pendingAcquireTimeout";
        String MAX_CONNECTIONS_KEY = "maxConnections";
        String PENDING_ACQUIRE_MAX_COUNT_KEY = "pendingAcquireMaxCount";
        String MAX_IDLE_TIME_KEY = "maxIdleTime";
        String MAX_LIFE_TIME_KEY = "maxLifeTime";
        String metrics_KEY = "metrics";
        String LEASING_STRATEGY_KEY = "leasingStrategy";
        String EVICTION_KEY = "eviction";
        String EVICTION_INTERVAL_KEY = "evictionInterval";
        String WORKERS_COUNT_KEY = "workersCount";
        String SELECTORS_COUNT_KEY = "selectorsCount";
        String CONNECTION_PROVIDER_NAME_KEY = "connectionProviderName";
        String LOOP_RESOURCES_PREFIX_KEY = "loopResourcesPrefix";
        String DISPOSE_INACTIVE_KEY = "disposeInactive";
        String DISPOSE_INTERVAL_KEY = "disposeInterval";
        String POOL_INACTIVITY_KEY = "poolInactivity";
        String BUFFER_WRITE_SECTION = "buffer.write";
        String BUFFER_TYPE_KEY = "type";
        String BUFFER_INITIAL_CAPACITY_KEY = "initialCapacity";
        String BUFFER_MAX_CAPACITY_KEY = "maxCapacity";
    }

    @Getter
    @AllArgsConstructor
    enum DataFormat {
        JSON("json"),
        YAML("yaml"),
        MESSAGE_PACK("messagePack");

        private final String format;

        public static DataFormat dataFormat(String format, DataFormat fallback) {
            if (JSON.format.equalsIgnoreCase(format)) return JSON;
            if (YAML.format.equalsIgnoreCase(format)) return YAML;
            if (MESSAGE_PACK.format.equalsIgnoreCase(format)) return MESSAGE_PACK;
            return fallback;
        }
    }

    @Getter
    @AllArgsConstructor
    enum BufferType {
        DEFAULT("default"),
        HEAP("heap"),
        IO("io"),
        DIRECT("direct");

        private final String type;

        public static BufferType bufferType(String type, BufferType fallback) {
            if (DEFAULT.type.equalsIgnoreCase(type)) return DEFAULT;
            if (HEAP.type.equalsIgnoreCase(type)) return HEAP;
            if (IO.type.equalsIgnoreCase(type)) return IO;
            if (DIRECT.type.equalsIgnoreCase(type)) return DIRECT;
            return fallback;
        }
    }

    interface GraalConstants {
        int EUI64_MAC_ADDRESS_LENGTH = 8;
        String MAX_UPDATE_ARRAY_SIZE_PROPERTY = "sun.nio.ch.maxUpdateArraySize";
        String DEFAULT_MAX_UPDATE_ARRAY_SIZE = "100";
        String NETTY_MAX_ORDER_PROPERTY = "io.netty.allocator.maxOrder";
        String DEFAULT_NETTY_MAX_ORDER = "100";
        String NETTY_MACHINE_ID_PROPERTY = "io.netty.machineId";
        String NETTY_LEAK_DETECTION_PROPERTY = "io.netty.leakDetection.level";
        String DEFAULT_NETTY_LEAK_DETECTION = "DISABLED";

        String NETTY_SSL_ENGINE_TYPE_CLASS = "io.netty.handler.ssl.SslHandler$SslEngineType";
        String NETTY_CHANNEL_HANDLER_MASK_CLASS = "io.netty.channel.ChannelHandlerMask";
        String NETTY_SCHEDULER_FUTURE_TASK_CLASS = "io.netty.util.concurrent.ScheduledFutureTask";
        String NETTY_REF_CNT_NAME = "refCnt";
        String NETTY_JDK_DEFAULT_APPLICATION_PROTOCOL_NEGOTIATOR_CLASS = "io.netty.handler.ssl.JdkDefaultApplicationProtocolNegotiator";
        String NETTY_JDK_ALPN_PROTOCOL_NEGOTIATOR_ALPN_WRAPPER_CLASS = "io.netty.handler.ssl.JdkAlpnApplicationProtocolNegotiator$AlpnWrapper";
        String NETTY_SSL_JETTY_ALPN_SSL_ENGINE_CLASS = "io.netty.handler.ssl.JettyAlpnSslEngine";
        String NETTY_SSL_JDK_ALPN_SSL_ENGINE_CLASS = "io.netty.handler.ssl.JdkAlpnSslEngine";
        String NETTY_DIR_CONTEXT_UTILS_CLASS = "io.netty.resolver.dns.DirContextUtils";

        String NETTY_OSCP_EXCEPTION = "OCSP is not supported with this SslProvider: {0}";
        String NETTY_JDK_SSL_PROVIDER_EXCEPTION = "JDK provider does not support ";
        String NETTY_JDK_SSL_FAILURE_BEHAVIOR_EXCEPTION = " failure behavior";
        String NETTY_JDK_SSL_PROTOCOL_EXCEPTION = " protocol";
        String NETTY_SSL_PROVIDER_UNSUPPORTED_EXCEPTION = "SslProvider unsupported: {0}";
        String NETTY_OPEN_SSL_UNSUPPORTED_EXCEPTION = "OpenSSL unsupported";
        String NETTY_UNABLE_TO_WRAP_SSL_ENGINE_EXCEPTION = "Unable to wrap SSLEngine of type {0}";
    }
}
