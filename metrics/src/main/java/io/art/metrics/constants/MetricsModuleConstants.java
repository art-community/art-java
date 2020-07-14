/*
 * ART Java
 *
 * Copyright 2019 ART
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

package io.art.metrics.constants;

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
