package ru.adk.metrics.constants;

public interface MetricsModuleConstants {
    String METRICS_MODULE_ID = "METRICS_MODULE";
    String METRICS_SERVICE_ID = "METRICS_SERVICE";
    String METRICS_METHOD_ID = "GET_METRICS";
    String METRICS_PATH = "/metrics";
    String MODULE_TAG = "module";
    String GC_METRICS = "gc";
    String THREADS_METRICS = "threads";
    String MEMORY_METRICS = "memory";
    String CIRCUIT_BREAKER_METRICS = "circuit_breaker";
    String RATE_LIMITER_METRICS = "rate_limiter";
    String RETRY_METRICS = "retry";
    int THREAD_METRICS_INTERVAL_GAUGE_SET = 10;
}
