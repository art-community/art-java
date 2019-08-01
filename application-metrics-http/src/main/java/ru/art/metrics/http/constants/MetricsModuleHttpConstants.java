package ru.art.metrics.http.constants;

import org.apache.http.entity.ContentType;
import ru.art.http.constants.MimeToContentTypeMapper;
import ru.art.http.mime.MimeType;
import static io.prometheus.client.exporter.common.TextFormat.CONTENT_TYPE_004;

public interface MetricsModuleHttpConstants {
    MimeToContentTypeMapper METRICS_CONTENT_TYPE = new MimeToContentTypeMapper(MimeType.valueOf(CONTENT_TYPE_004), ContentType.parse(CONTENT_TYPE_004));
}
