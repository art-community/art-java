/*
 *    Copyright 2019 ART
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package ru.art.service.model;

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
