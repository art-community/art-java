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

package io.art.rsocket.configuration.common;

import io.art.core.source.*;
import lombok.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.rsocket.constants.RsocketModuleConstants.ConfigurationKeys.*;
import static io.art.rsocket.constants.RsocketModuleConstants.Defaults.*;
import java.time.*;

@Getter
@Builder(toBuilder = true)
public class RsocketKeepAliveConfiguration {
    private final Duration interval;
    private final Duration maxLifeTime;

    public static RsocketKeepAliveConfiguration rsocketKeepAlive() {
        return new RsocketKeepAliveConfiguration(DEFAULT_KEEP_ALIVE_INTERVAL, DEFAULT_KEEP_ALIVE_MAX_LIFE_TIME);
    }

    public static RsocketKeepAliveConfiguration rsocketKeepAlive(ConfigurationSource source) {
        Duration interval = orElse(source.getDuration(INTERVAL_KEY), DEFAULT_KEEP_ALIVE_INTERVAL);
        Duration maxLifeTime = orElse(source.getDuration(MAX_LIFE_TIME_KEY), DEFAULT_KEEP_ALIVE_MAX_LIFE_TIME);
        return new RsocketKeepAliveConfiguration(interval, maxLifeTime);
    }

    public static RsocketKeepAliveConfiguration rsocketKeepAlive(ConfigurationSource source, RsocketKeepAliveConfiguration defaultKeepAlive) {
        Duration interval = orElse(source.getDuration(INTERVAL_KEY), defaultKeepAlive.interval);
        Duration maxLifeTime = orElse(source.getDuration(MAX_LIFE_TIME_KEY), defaultKeepAlive.maxLifeTime);
        return new RsocketKeepAliveConfiguration(interval, maxLifeTime);
    }
}
