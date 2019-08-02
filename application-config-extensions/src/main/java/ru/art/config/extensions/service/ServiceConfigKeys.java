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

package ru.art.config.extensions.service;

public interface ServiceConfigKeys {
    String DEACTIVATION = "deactivation";
    String DEACTIVATED = "deactivated";
    String METHODS = "methods";
    String SERVICE = "service";
    String FAILURE_RATE_THRESHOLD = "failureRateThreshold";
    String WAIT_DURATION_IN_OPEN_STATE = "waitDurationInOpenState";
    String RING_BUFFER_SIZE_IN_HALF_OPEN_STATE = "ringBufferSizeInHalfOpenState";
    String RING_BUFFER_SIZE_IN_CLOSED_STATE = "ringBufferSizeInClosedState";
    String AUTOMATIC_TRANSITION_FROM_OPEN_TO_HALF_OPEN_ENABLED = "automaticTransitionFromOpenToHalfOpenEnabled";
    String TIMEOUT_DURATION = "timeoutDuration";
    String LIMIT_REFRESH_PERIOD = "limitRefreshPeriod";
    String LIMIT_FOR_PERIOD = "limitForPeriod";
    String MAX_ATTEMPTS = "maxAttempts";
    String WAIT_DURATION = "waitDuration";
    String MAX_CONCURRENT_CALLS = "maxConcurrentCalls";
    String MAX_WAIT_TIME = "waitDuration";
    String BREAKABLE = "breakable";
    String LIMITED = "limited";
    String BULKHEADED = "bulkheaded";
    String RETRYABLE = "retryable";
}
