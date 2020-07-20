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
public class CircuitBreakerConfiguration {
    private boolean breakable;
    private CircuitBreakerConfig configuration;

    public static Builder builder() {
        return new Builder();
    }

    @Setter
    @Accessors(chain = true, fluent = true)
    public static class Builder {
        private boolean breakable;
        private CircuitBreakerConfig.Builder builder;

        public CircuitBreakerConfiguration build() {
            return new CircuitBreakerConfiguration(this.breakable, builder.build());
        }
    }
}
