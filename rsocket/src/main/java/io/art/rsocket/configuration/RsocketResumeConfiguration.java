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

package io.art.rsocket.configuration;

import io.art.core.source.*;
import io.rsocket.core.*;
import lombok.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.rsocket.constants.RsocketModuleConstants.ConfigurationKeys.*;
import static io.art.rsocket.constants.RsocketModuleConstants.Defaults.*;
import java.time.*;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class RsocketResumeConfiguration {
    boolean cleanupStoreOnKeepAlive;
    Duration sessionDuration;
    Duration streamTimeout;
    RsocketRetryConfiguration retryConfiguration;

    public Resume toResume() {
        Resume resume = new Resume()
                .streamTimeout(streamTimeout)
                .sessionDuration(sessionDuration);
        if (cleanupStoreOnKeepAlive) {
            resume.cleanupStoreOnKeepAlive();
        }
        apply(retryConfiguration, configuration -> resume.retry(configuration.toRetry()));
        return resume;
    }

    public static RsocketResumeConfiguration rsocketResume(ConfigurationSource source) {
        RsocketResumeConfiguration configuration = new RsocketResumeConfiguration();
        configuration.cleanupStoreOnKeepAlive = orElse(source.getBool(CLEANUP_STORE_ON_KEEP_ALIVE_KEY), false);
        configuration.sessionDuration = orElse(source.getDuration(SESSION_DURATION_KEY), DEFAULT_RESUME_SESSION_DURATION);
        configuration.streamTimeout = orElse(source.getDuration(STREAM_TIMEOUT_KEY), DEFAULT_RESUME_STREAM_TIMEOUT);
        configuration.retryConfiguration = let(source.getNested(RETRY_SECTION), RsocketRetryConfiguration::rsocketRetry);
        return configuration;
    }

    public static RsocketResumeConfiguration rsocketResume(ConfigurationSource source, RsocketResumeConfiguration defaults) {
        RsocketResumeConfiguration configuration = new RsocketResumeConfiguration();
        configuration.cleanupStoreOnKeepAlive = orElse(source.getBool(CLEANUP_STORE_ON_KEEP_ALIVE_KEY), defaults.cleanupStoreOnKeepAlive);
        configuration.sessionDuration = orElse(source.getDuration(SESSION_DURATION_KEY), defaults.sessionDuration);
        configuration.streamTimeout = orElse(source.getDuration(STREAM_TIMEOUT_KEY), defaults.streamTimeout);
        configuration.retryConfiguration = let(source.getNested(RETRY_SECTION), RsocketRetryConfiguration::rsocketRetry, defaults.retryConfiguration);
        return configuration;
    }
}
