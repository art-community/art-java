package ru.adk.service.model;

import io.github.resilience4j.bulkhead.BulkheadConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BulkheadServiceConfig {
    private boolean bulkheaded;
    private BulkheadConfig bulkheadConfig;

    public static BulkheadServiceConfigBuilder builder() {
        return new BulkheadServiceConfigBuilder();
    }

    @Setter
    @Accessors(chain = true, fluent = true)
    public static class BulkheadServiceConfigBuilder {
        private boolean bulkheaded;
        private BulkheadConfig.Builder bulkheadConfigBuilder;

        public BulkheadServiceConfig build() {
            return new BulkheadServiceConfig(this.bulkheaded, this.bulkheadConfigBuilder.build());
        }
    }
}
