package ru.adk.http.server;

import io.micrometer.core.instrument.binder.tomcat.TomcatMetrics;
import org.apache.catalina.Manager;
import static java.util.Collections.emptyList;
import static ru.adk.metrics.module.MetricsModule.metricsModule;

public interface HttpMetricsBinder {
    static void bindHttpMetrics(Manager tomcatManager) {
        new TomcatMetrics(tomcatManager, emptyList()).bindTo(metricsModule().getPrometheusMeterRegistry());
    }
}
