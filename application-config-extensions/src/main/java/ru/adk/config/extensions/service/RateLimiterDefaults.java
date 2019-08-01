package ru.adk.config.extensions.service;

import static java.time.Duration.ofNanos;
import static java.time.Duration.ofSeconds;
import java.time.Duration;

interface RateLimiterDefaults {
    Duration DEFAULT_TIMEOUT_DURATION = ofSeconds(5);
    Duration DEFAULT_LIMIT_REFRESH_PERIOD = ofNanos(500);
    int DEFAULT_LIMIT_FOR_PERIOD = 50;
}