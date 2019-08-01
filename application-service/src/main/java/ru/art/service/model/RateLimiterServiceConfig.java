package ru.art.service.model;

import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RateLimiterServiceConfig {
    private boolean limited;
    private RateLimiterConfig rateLimiterConfig;

    public static RateLimiterServiceConfigBuilder builder() {
        return new RateLimiterServiceConfigBuilder();
    }

    @Setter
    @Accessors(chain = true, fluent = true)
    public static class RateLimiterServiceConfigBuilder {
        private boolean limited;
        private RateLimiterConfig.Builder rateLimiterConfigBuilder;

        public RateLimiterServiceConfig build() {
            return new RateLimiterServiceConfig(this.limited, this.rateLimiterConfigBuilder.build());
        }
    }
}
