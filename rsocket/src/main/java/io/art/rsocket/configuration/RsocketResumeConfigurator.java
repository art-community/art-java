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

package io.art.rsocket.configuration;

import io.art.core.source.*;
import io.rsocket.core.*;
import lombok.experimental.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.rsocket.constants.RsocketModuleConstants.ConfigurationKeys.*;
import static io.art.rsocket.constants.RsocketModuleConstants.Defaults.*;
import java.time.*;

@UtilityClass
public class RsocketResumeConfigurator {
    public static Resume from(ConfigurationSource source) {
        boolean cleanupStoreOnKeepAlive = orElse(source.getBool(CLEANUP_STORE_ON_KEEP_ALIVE_KEY), false);
        Duration sessionDuration = orElse(source.getDuration(SESSION_DURATION_KEY), DEFAULT_RESUME_SESSION_DURATION);
        Duration streamTimeout = orElse(source.getDuration(STREAM_TIMEOUT_KEY), DEFAULT_RESUME_STREAM_TIMEOUT);
        Resume resume = new Resume()
                .streamTimeout(streamTimeout)
                .sessionDuration(sessionDuration);
        if (cleanupStoreOnKeepAlive) {
            resume.cleanupStoreOnKeepAlive();
        }
        apply(source.getNested(RETRY_SECTION), retry -> resume.retry(RsocketRetryConfigurator.from(retry)));
        return resume;
    }

    public static Resume from(ConfigurationSource source, Resume defaultResume) {
        if (orElse(source.getBool(CLEANUP_STORE_ON_KEEP_ALIVE_KEY), false)) {
            defaultResume.cleanupStoreOnKeepAlive();
        }
        apply(source.getDuration(SESSION_DURATION_KEY), defaultResume::sessionDuration);
        apply(source.getDuration(SESSION_DURATION_KEY), defaultResume::streamTimeout);
        apply(source.getNested(RETRY_SECTION), retry -> defaultResume.retry(RsocketRetryConfigurator.from(retry)));
        return defaultResume;
    }
}
