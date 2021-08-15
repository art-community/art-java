package io.art.transport.constants;

import lombok.*;
import static io.art.core.constants.ThreadConstants.*;
import static java.lang.Math.*;
import static java.time.Duration.*;
import static reactor.netty.resources.ConnectionProvider.*;
import java.time.*;

public interface TransportModuleConstants {
    interface Defaults {
        String DEFAULT_LOOP_RESOURCES_PREFIX = "common-transport-";
        String DEFAULT_CONNECTION_PROVIDER_NAME = "common-connection";
        int DEFAULT_SELECTORS_COUNT = 3;
        int DEFAULT_WORKERS_COUNT = (int) max(ceil(DEFAULT_THREAD_POOL_SIZE * 0.25), 2);
        Duration DEFAULT_EVICTION_INTERVAL = ZERO;
        int DEFAULT_MAX_CONNECTIONS = DEFAULT_THREAD_POOL_SIZE * 2;
        int DEFAULT_PENDING_ACQUIRE_MAX_COUNT = -2;
        Duration DEFAULT_PENDING_ACQUIRE_TIMEOUT = Duration.ofSeconds(30);
        Duration DEFAULT_MAX_LIFE_TIME = Duration.ofMillis(-1);
        Duration DEFAULT_MAX_IDLE_TIME = Duration.ofMillis(-1);
        String DEFAULT_LEASING_STRATEGY = LEASING_STRATEGY_FIFO;
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
}
