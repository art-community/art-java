package ru.art.http.configuration;

import lombok.Getter;
import org.zalando.logbook.Logbook;
import ru.art.core.module.ModuleConfiguration;
import ru.art.http.logger.ZalangoLogbookLogWriter;
import ru.art.http.mapper.HttpContentMapper;
import ru.art.http.mapper.HttpTextPlainMapper;
import ru.art.http.mime.MimeType;
import static ru.art.core.factory.CollectionsFactory.mapOf;
import static ru.art.http.constants.HttpMimeTypes.ALL;
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