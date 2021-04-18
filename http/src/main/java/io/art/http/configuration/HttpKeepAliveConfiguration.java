/*
 * ART
 *
 * Copyright 2019-2021 ART
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

package io.art.http.configuration;

import io.art.core.source.*;
import lombok.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.http.constants.HttpModuleConstants.ConfigurationKeys.*;
import static io.art.http.constants.HttpModuleConstants.Defaults.*;
import java.time.*;

@Value
@RequiredArgsConstructor
public class HttpKeepAliveConfiguration {
    Duration interval;
    Duration maxLifeTime;

    public static HttpKeepAliveConfiguration httpKeepAlive(ConfigurationSource source) {
        Duration interval = orElse(source.getDuration(INTERVAL_KEY), DEFAULT_KEEP_ALIVE_INTERVAL);
        Duration maxLifeTime = orElse(source.getDuration(MAX_LIFE_TIME_KEY), DEFAULT_KEEP_ALIVE_MAX_LIFE_TIME);
        return new HttpKeepAliveConfiguration(interval, maxLifeTime);
    }

    public static HttpKeepAliveConfiguration httpKeepAlive(ConfigurationSource source, HttpKeepAliveConfiguration defaultKeepAlive) {
        Duration interval = orElse(source.getDuration(INTERVAL_KEY), defaultKeepAlive.interval);
        Duration maxLifeTime = orElse(source.getDuration(MAX_LIFE_TIME_KEY), defaultKeepAlive.maxLifeTime);
        return new HttpKeepAliveConfiguration(interval, maxLifeTime);
    }
}
