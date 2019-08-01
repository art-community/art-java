package ru.adk.config.extensions.service;

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
