package io.art.transport.constants;

import lombok.*;

public interface TransportModuleConstants {
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
