/*
 * ART
 *
 * Copyright 2019-2021 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.art.rsocket.configuration.common;

import io.art.core.exception.*;
import io.art.core.source.*;
import lombok.*;
import reactor.util.retry.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.rsocket.constants.RsocketModuleConstants.ConfigurationKeys.*;
import static io.art.rsocket.constants.RsocketModuleConstants.Defaults.*;
import static io.art.rsocket.constants.RsocketModuleConstants.*;
import static io.art.rsocket.constants.RsocketModuleConstants.RetryPolicy.*;
import static reactor.util.retry.Retry.*;
import java.time.*;

@Getter
@Builder(toBuilder = true)
public class RsocketRetryConfiguration {
    private Duration minBackoff;
    private long backOffMaxAttempts;
    private long fixedDelayMaxAttempts;
    private RetryPolicy retryPolicy;
    private Duration fixedDelay;
    private int maxInRow;
    private int max;

    public Retry toRetry() {
        switch (retryPolicy) {
            case BACKOFF:
                return backoff(backOffMaxAttempts, minBackoff);
            case FIXED_DELAY:
                return fixedDelay(fixedDelayMaxAttempts, fixedDelay);
            case MAX:
                return max(max);
            case MAX_IN_A_ROW:
                return maxInARow(maxInRow);
            case INDEFINITELY:
                return indefinitely();
        }
        throw new ImpossibleSituationException();
    }

    public static RsocketRetryConfiguration rsocketRetry() {
        return RsocketRetryConfiguration.builder()
                .retryPolicy(INDEFINITELY)
                .build();
    }

    public static RsocketRetryConfiguration rsocketRetry(ConfigurationSource source) {
        RsocketRetryConfiguration configuration = RsocketRetryConfiguration.builder().build();
        configuration.retryPolicy = rsocketRetryPolicy(source.getString(POLICY_KEY), INDEFINITELY);
        configuration.backOffMaxAttempts = orElse(source.getLong(BACKOFF_MAX_ATTEMPTS_KEY), DEFAULT_RETRY_MAX_ATTEMPTS);
        configuration.fixedDelayMaxAttempts = orElse(source.getLong(FIXED_DELAY_MAX_ATTEMPTS_KEY), DEFAULT_RETRY_MAX_ATTEMPTS);
        configuration.minBackoff = orElse(source.getDuration(BACKOFF_MIN_BACKOFF_KEY), DEFAULT_RETRY_MIN_BACKOFF);
        configuration.fixedDelay = orElse(source.getDuration(FIXED_DELAY_KEY), DEFAULT_RETRY_FIXED_DELAY);
        configuration.max = orElse(source.getInteger(MAX_KEY), DEFAULT_RETRY_MAX);
        configuration.maxInRow = orElse(source.getInteger(MAX_IN_ROW_KEY), DEFAULT_RETRY_MAX_IN_ROW);
        return configuration;
    }

    public static RsocketRetryConfiguration rsocketRetry(ConfigurationSource source, RsocketRetryConfiguration defaults) {
        RsocketRetryConfiguration configuration = RsocketRetryConfiguration.builder().build();
        configuration.retryPolicy = rsocketRetryPolicy(source.getString(POLICY_KEY), defaults.retryPolicy);
        configuration.backOffMaxAttempts = orElse(source.getLong(BACKOFF_MAX_ATTEMPTS_KEY), defaults.backOffMaxAttempts);
        configuration.fixedDelayMaxAttempts = orElse(source.getLong(FIXED_DELAY_MAX_ATTEMPTS_KEY), defaults.fixedDelayMaxAttempts);
        configuration.minBackoff = orElse(source.getDuration(BACKOFF_MIN_BACKOFF_KEY), defaults.minBackoff);
        configuration.fixedDelay = orElse(source.getDuration(FIXED_DELAY_KEY), defaults.fixedDelay);
        configuration.max = orElse(source.getInteger(MAX_KEY), defaults.max);
        configuration.maxInRow = orElse(source.getInteger(MAX_IN_ROW_KEY), defaults.maxInRow);
        return configuration;
    }
}
