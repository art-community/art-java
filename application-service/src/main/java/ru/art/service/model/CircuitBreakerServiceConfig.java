package ru.art.service.model;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CircuitBreakerServiceConfig {
    private boolean breakable;
    private CircuitBreakerConfig circuitBreakerConfig;

    public static CircuitBreakerServiceConfigBuilder builder() {
        return new CircuitBreakerServiceConfigBuilder();
    }

    @Setter
    @Accessors(chain = true, fluent = true)
    public static class CircuitBreakerServiceConfigBuilder {
        private boolean breakable;
        private CircuitBreakerConfig.Builder circuitBreakerConfigBuilder;

        public CircuitBreakerServiceConfig build() {
            return new CircuitBreakerServiceConfig(this.breakable, circuitBreakerConfigBuilder.build());
        }
    }
}
