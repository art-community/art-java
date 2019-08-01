package ru.adk.metrics.http.filter;

import org.zalando.logbook.Logbook;
import org.zalando.logbook.LogbookCreator.Builder;
import ru.adk.http.logger.ZalangoLogbookLogWriter;
import static org.zalando.logbook.Conditions.exclude;
import static org.zalando.logbook.Conditions.requestTo;
import static ru.adk.metrics.constants.MetricsModuleConstants.METRICS_PATH;
import static ru.adk.metrics.module.MetricsModule.metricsModule;

public interface MetricsHttpLogFilter {
    static Builder logbookWithoutMetricsLogs(Builder builder) {
        return builder
                .condition(exclude(requestTo(metricsModule().getPath() + METRICS_PATH)))
                .writer(new ZalangoLogbookLogWriter());
    }

    static Builder logbookWithoutMetricsLogs() {
        return Logbook.builder()
                .condition(exclude(requestTo(metricsModule().getPath() + METRICS_PATH)))
                .writer(new ZalangoLogbookLogWriter());
    }
}
