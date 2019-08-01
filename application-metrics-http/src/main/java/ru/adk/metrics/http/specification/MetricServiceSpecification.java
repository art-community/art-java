package ru.adk.metrics.http.specification;

import lombok.Getter;
import ru.adk.http.server.model.HttpService;
import ru.adk.http.server.specification.HttpServiceSpecification;
import ru.adk.service.exception.UnknownServiceMethodException;
import static java.util.Collections.emptyList;
import static ru.adk.core.caster.Caster.cast;
import static ru.adk.entity.PrimitiveMapping.stringMapper;
import static ru.adk.http.server.model.HttpService.httpService;
import static ru.adk.metrics.constants.MetricsModuleConstants.*;
import static ru.adk.metrics.http.constants.MetricsModuleHttpConstants.METRICS_CONTENT_TYPE;
import static ru.adk.metrics.module.MetricsModule.metricsModule;
import static ru.adk.service.interceptor.ServiceExecutionInterceptor.ServiceRequestInterceptor;
import static ru.adk.service.interceptor.ServiceExecutionInterceptor.ServiceResponseInterceptor;
import java.util.List;

@Getter
public class MetricServiceSpecification implements HttpServiceSpecification {
    private final String serviceId = METRICS_SERVICE_ID;
    private final List<ServiceRequestInterceptor> requestInterceptors = emptyList();
    private final List<ServiceResponseInterceptor> responseInterceptors = emptyList();

    private final HttpService httpService = httpService()
            .get(METRICS_METHOD_ID)
            .produces(METRICS_CONTENT_TYPE)
            .responseMapper(stringMapper.getFromModel())
            .listen(METRICS_PATH)
            .serve(metricsModule().getPath());

    @Override
    public <P, R> R executeMethod(String methodId, P request) {
        if (METRICS_METHOD_ID.equals(methodId)) {
            return cast(metricsModule().getPrometheusMeterRegistry().scrape());
        }
        throw new UnknownServiceMethodException(METRICS_SERVICE_ID, methodId);
    }
}
