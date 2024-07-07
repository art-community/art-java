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
    interface Defaults {
        String DEFAULT_LOOP_RESOURCES_PREFIX = "common-transport";
        String DEFAULT_CONNECTION_PROVIDER_NAME = "common-connection";
        int DEFAULT_SELECTORS_COUNT = 2;
        int DEFAULT_WORKERS_COUNT = (int) max(ceil((double) DEFAULT_THREAD_POOL_SIZE / 2), 2);
        Duration DEFAULT_EVICTION_INTERVAL = ZERO;
        int DEFAULT_MAX_CONNECTIONS = 1024;
        int DEFAULT_PENDING_ACQUIRE_MAX_COUNT = -2;
        Duration DEFAULT_PENDING_ACQUIRE_TIMEOUT = ofSeconds(30);
        Duration DEFAULT_MAX_LIFE_TIME = ofMillis(-1);
        Duration DEFAULT_MAX_IDLE_TIME = ofMillis(-1);
        String DEFAULT_LEASING_STRATEGY = LEASING_STRATEGY_FIFO;
        BufferType DEFAULT_BUFFER_TYPE = DIRECT;
        int DEFAULT_BUFFER_INITIAL_CAPACITY = 256;
        int DEFAULT_BUFFER_MAX_CAPACITY = MAX_VALUE;
        long DEFAULT_RETRY_MAX_ATTEMPTS = 10;
        Duration DEFAULT_RETRY_MIN_BACKOFF = ofSeconds(1);
        Duration DEFAULT_RETRY_FIXED_DELAY = ofSeconds(1);
        int DEFAULT_RETRY_MAX = 100;
        int DEFAULT_RETRY_MAX_IN_ROW = 10;
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

        String SSL_SECTION = "ssl";
        String DATA_FORMAT_KEY = "dataFormat";
        String INPUT_DATA_FORMAT_KEY = "inputDataFormat";
        String OUTPUT_DATA_FORMAT_KEY = "outputDataFormat";
        String META_DATA_FORMAT_KEY = "metaDataFormat";
        String SERVICE_ID_KEY = "serviceId";
        String METHOD_ID_KEY = "methodId";
        String TIMEOUT_KEY = "timeout";
        String PAYLOAD_DECODER_KEY = "payloadDecoder";
        String CERTIFICATE_KEY = "certificate";
        String KEY_KEY = "key";
        String PASSWORD_KEY = "password";
        String DEACTIVATED_KEY = "deactivated";
        String LOGGING_KEY = "logging";
        String POLICY_KEY = "policy";
        String BACKOFF_MAX_ATTEMPTS_KEY = "backoff.maxAttempts";
        String BACKOFF_MIN_BACKOFF_KEY = "backoff.minBackoff";
        String FIXED_DELAY_MAX_ATTEMPTS_KEY = "fixedDelay.maxAttempts";
        String FIXED_DELAY_KEY = "fixedDelay.delay";
        String MAX_KEY = "max";
        String MAX_IN_ROW_KEY = "maxInRow";
    }

    @Getter
    @AllArgsConstructor
    enum DataFormat {
        JSON("json"),
        YAML("yaml"),
        MESSAGE_PACK("messagePack"),
        STRING("string"),
        BYTES("bytes");

        private final String format;

        public static DataFormat dataFormat(String format, DataFormat fallback) {
            if (JSON.format.equalsIgnoreCase(format)) return JSON;
            if (YAML.format.equalsIgnoreCase(format)) return YAML;
            if (MESSAGE_PACK.format.equalsIgnoreCase(format)) return MESSAGE_PACK;
            if (BYTES.format.equalsIgnoreCase(format)) return BYTES;
            if (STRING.format.equalsIgnoreCase(format)) return STRING;
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

    @Getter
    @AllArgsConstructor
    enum RetryPolicy {
        BACKOFF("backoff"),
        FIXED_DELAY("fixedDelay"),
        MAX("max"),
        MAX_IN_A_ROW("maxInARow"),
        INDEFINITELY("indefinitely");

        private final String policy;

        public static RetryPolicy retryPolicy(String policy, RetryPolicy fallback) {
            if (BACKOFF.policy.equalsIgnoreCase(policy)) return BACKOFF;
            if (FIXED_DELAY.policy.equalsIgnoreCase(policy)) return FIXED_DELAY;
            if (MAX.policy.equalsIgnoreCase(policy)) return MAX;
            if (MAX_IN_A_ROW.policy.equalsIgnoreCase(policy)) return MAX_IN_A_ROW;
            if (INDEFINITELY.policy.equalsIgnoreCase(policy)) return INDEFINITELY;
            return fallback;
        }
    }
}
