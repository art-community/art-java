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

package io.art.rsocket.configurator;

import io.art.rsocket.configuration.*;
import io.rsocket.core.*;
import static reactor.util.retry.Retry.*;

public class RsocketServerConfigurator {
    public static Resume configureResume(RsocketResumeConfiguration resumeConfiguration) {
        Resume resume = new Resume()
                .streamTimeout(resumeConfiguration.getStreamTimeout())
                .sessionDuration(resumeConfiguration.getSessionDuration());
        switch (resumeConfiguration.getRetryPolicy()) {
            case BACKOFF:
                RsocketResumeConfiguration.RetryBackoffConfiguration backoffConfiguration = resumeConfiguration.getRetryBackoffConfiguration();
                resume = resume.retry(backoff(backoffConfiguration.getMaxAttempts(), backoffConfiguration.getMinBackoff()));
                break;
            case FIXED_DELAY:
                RsocketResumeConfiguration.RetryFixedDelayConfiguration fixedDelayConfiguration = resumeConfiguration.getRetryFixedDelayConfiguration();
                resume = resume.retry(fixedDelay(fixedDelayConfiguration.getMaxAttempts(), fixedDelayConfiguration.getFixedDelay()));
                break;
            case MAX:
                RsocketResumeConfiguration.RetryMaxConfiguration maxConfiguration = resumeConfiguration.getRetryMaxConfiguration();
                resume = resume.retry(max(maxConfiguration.getMax()));
                break;
            case MAX_IN_A_ROW:
                RsocketResumeConfiguration.RetryMaxInRowConfiguration maxInRowConfiguration = resumeConfiguration.getRetryMaxInRowConfiguration();
                resume = resume.retry(maxInARow(maxInRowConfiguration.getMaxInRow()));
                break;
            case INDEFINITELY:
                resume = resume.retry(indefinitely());
                break;
        }
        return resume;
    }
}
