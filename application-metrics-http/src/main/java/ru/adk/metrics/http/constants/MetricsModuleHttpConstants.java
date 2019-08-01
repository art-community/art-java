package ru.adk.metrics.http.constants;

import org.apache.http.entity.ContentType;
import ru.adk.http.constants.MimeToContentTypeMapper;
import ru.adk.http.mime.MimeType;
import static io.prometheus.client.exporter.common.TextFormat.CONTENT_TYPE_004;

public interface MetricsModuleHttpConstants {
    MimeToContentTypeMapper METRICS_CONTENT_TYPE = new MimeToContentTypeMapper(MimeType.valueOf(CONTENT_TYPE_004), ContentType.parse(CONTENT_TYPE_004));
}
