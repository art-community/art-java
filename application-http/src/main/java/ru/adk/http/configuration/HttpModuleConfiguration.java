package ru.adk.http.configuration;

import lombok.Getter;
import org.zalando.logbook.Logbook;
import ru.adk.core.module.ModuleConfiguration;
import ru.adk.http.logger.ZalangoLogbookLogWriter;
import ru.adk.http.mapper.HttpContentMapper;
import ru.adk.http.mapper.HttpTextPlainMapper;
import ru.adk.http.mime.MimeType;
import static ru.adk.core.factory.CollectionsFactory.mapOf;
import static ru.adk.http.constants.HttpMimeTypes.ALL;
import java.util.Map;

public interface HttpModuleConfiguration extends ModuleConfiguration {
    boolean isEnableTracing();

    @SuppressWarnings("EmptyMethod")
    boolean isEnableMetricsMonitoring();

    Map<MimeType, HttpContentMapper> getContentMappers();

    Logbook getLogbook();

    @Getter
    class HttpModuleDefaultConfiguration implements HttpModuleConfiguration {
        private final boolean enableTracing = true;
        private final boolean enableMetricsMonitoring = true;
        private final HttpTextPlainMapper textPlainMapper = new HttpTextPlainMapper();
        private final Map<MimeType, HttpContentMapper> contentMappers = mapOf(ALL, new HttpContentMapper(new HttpTextPlainMapper(), new HttpTextPlainMapper()));
        @Getter(lazy = true)
        private final Logbook logbook = Logbook.builder().writer(new ZalangoLogbookLogWriter()).build();
    }
}