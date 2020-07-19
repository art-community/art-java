/*
 * ART
 *
 * Copyright 2020 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.art.server.model;

import io.github.resilience4j.circuitbreaker.*;
import lombok.*;
import lombok.experimental.*;

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
