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

package io.art.metrics.http.specification;

import lombok.*;
import io.art.http.server.model.*;
import io.art.http.server.specification.*;
import io.art.service.exception.*;
import static java.util.Collections.*;
import static io.art.core.caster.Caster.*;
import static io.art.entity.PrimitiveMapping.*;
import static io.art.http.server.model.HttpService.*;
import static io.art.metrics.constants.MetricsModuleConstants.*;
import static io.art.metrics.http.constants.MetricsModuleHttpConstants.*;
import static io.art.metrics.module.MetricsModule.*;
import static io.art.service.interceptor.ServiceExecutionInterceptor.*;
import java.util.*;

@Getter
@AllArgsConstructor
public class MetricServiceSpecification implements HttpServiceSpecification {
    private final String path;
    private final String serviceId = METRICS_SERVICE_ID;
    private final List<RequestInterceptor> requestInterceptors = emptyList();
    private final List<ResponseInterceptor> responseInterceptors = emptyList();
    @Getter(lazy = true)
    private final HttpService httpService = httpService()
            .get(METRICS_METHOD_ID)
            .produces(METRICS_CONTENT_TYPE)
            .responseMapper(stringMapper.getFromModel())
            .listen(METRICS_PATH)
            .serve(path);

    @Override
    public <P, R> R executeMethod(String methodId, P request) {
        if (METRICS_METHOD_ID.equals(methodId)) {
            return cast(metricsModule().getPrometheusMeterRegistry().scrape());
        }
        throw new UnknownServiceMethodException(METRICS_SERVICE_ID, methodId);
    }
}
