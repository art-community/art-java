package ru.adk.service.model;

import io.github.resilience4j.retry.RetryConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RetryServiceConfig {
    private boolean retryable;
    private RetryConfig retryConfig;

    public static RetryServiceConfigBuilder builder() {
        return new RetryServiceConfigBuilder();
    }

    @Setter
    @Accessors(chain = true, fluent = true)
    public static class RetryServiceConfigBuilder {
        private boolean retryable;
        private RetryConfig.Builder retryConfigBuilder;

        public RetryServiceConfig build() {
            return new RetryServiceConfig(this.retryable, this.retryConfigBuilder.build());
        }
    }
}
