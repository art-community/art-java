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
import lombok.experimental.*;
import reactor.util.retry.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.rsocket.constants.RsocketModuleConstants.ConfigurationKeys.*;
import static io.art.rsocket.constants.RsocketModuleConstants.Defaults.*;
import static io.art.rsocket.constants.RsocketModuleConstants.*;
import static io.art.rsocket.constants.RsocketModuleConstants.RetryPolicy.*;
import static java.util.Objects.*;
import static reactor.util.retry.Retry.*;
import java.time.*;

@UtilityClass
public class RsocketRetryConfigurator {
    public static Retry from(ConfigurationSource source) {
        RetryPolicy retryPolicy = rsocketRetryPolicy(source.getString(POLICY_KEY), INDEFINITELY);
        switch (retryPolicy) {
            case BACKOFF:
                long maxAttempts = orElse(source.getLong(BACKOFF_MAX_ATTEMPTS_KEY), DEFAULT_RETRY_MAX_ATTEMPTS);
                Duration minBackoff = orElse(source.getDuration(BACKOFF_MIN_BACKOFF_KEY), DEFAULT_RETRY_MIN_BACKOFF);
                return backoff(maxAttempts, minBackoff);
            case FIXED_DELAY:
                maxAttempts = orElse(source.getLong(FIXED_DELAY_MAX_ATTEMPTS_KEY), DEFAULT_RETRY_MAX_ATTEMPTS);
                Duration fixedDelay = orElse(source.getDuration(FIXED_DELAY_KEY), DEFAULT_RETRY_FIXED_DELAY);
                return fixedDelay(maxAttempts, fixedDelay);
            case MAX:
                int max = orElse(source.getInt(MAX_KEY), DEFAULT_RETRY_MAX);
                return max(max);
            case MAX_IN_A_ROW:
                int maxInRow = orElse(source.getInt(MAX_IN_ROW_KEY), DEFAULT_RETRY_MAX_IN_ROW);
                return maxInARow(maxInRow);
            case INDEFINITELY:
                return indefinitely();
        }
        throw new IllegalStateException();
    }

    public static Retry from(ConfigurationSource source, Retry defaultRetry) {
        RetryPolicy retryPolicy = rsocketRetryPolicy(source.getString(POLICY_KEY), INDEFINITELY);
        switch (retryPolicy) {
            case BACKOFF:
                Long maxAttempts = source.getLong(BACKOFF_MAX_ATTEMPTS_KEY);
                Duration minBackoff = source.getDuration(BACKOFF_MIN_BACKOFF_KEY);
                if (nonNull(maxAttempts) && nonNull(minBackoff)) {
                    return backoff(maxAttempts, minBackoff);
                }
                return defaultRetry;
            case FIXED_DELAY:
                maxAttempts = source.getLong(BACKOFF_MAX_ATTEMPTS_KEY);
                Duration fixedDelay = source.getDuration(FIXED_DELAY_KEY);
                if (nonNull(maxAttempts) && nonNull(fixedDelay)) {
                    return fixedDelay(maxAttempts, fixedDelay);
                }
                return defaultRetry;
            case MAX:
                Integer max = source.getInt(MAX_KEY);
                if (nonNull(max)) {
                    return max(max);
                }
                return defaultRetry;
            case MAX_IN_A_ROW:
                Integer maxInRow = source.getInt(MAX_IN_ROW_KEY);
                if (nonNull(maxInRow)) {
                    return maxInARow(maxInRow);
                }
                return defaultRetry;
            case INDEFINITELY:
                return indefinitely();
        }
        return defaultRetry;
    }
}
