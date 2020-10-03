/*
 * ART
 *
 * Copyright 2020 ART
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

package io.art.http.server.specification;

import io.art.entity.immutable.*;
import io.art.http.server.service.*;
import io.art.server.implementation.*;
import io.art.server.specification.*;
import lombok.*;
import io.art.core.caster.*;
import io.art.entity.mapper.ValueToModelMapper.*;
import io.art.http.server.HttpServerModuleConfiguration.*;
import io.art.http.server.model.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.http.server.constants.HttpServerModuleConstants.HttpResourceServiceConstants.*;
import static io.art.http.server.constants.HttpServerModuleConstants.HttpResourceServiceConstants.HttpParameters.*;
import static io.art.http.server.constants.HttpServerModuleConstants.HttpResourceServiceConstants.Methods.*;
import static io.art.http.server.extractor.HttpResponseContentTypeExtractor.*;
import static io.art.http.server.interceptor.HttpServerInterception.*;
import static io.art.http.server.interceptor.HttpServerInterceptor.*;
import static io.art.http.server.model.HttpService.*;
import static io.art.http.server.module.HttpServerModule.*;
import static io.art.http.server.service.HttpResourceService.*;
import static io.art.server.model.ServiceInterceptionResult.*;
import java.util.*;
import java.util.function.*;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class HttpResourceServiceSpecification {
    private final String resourcePath;
    private HttpResourceConfiguration resourceConfiguration;

    public ServiceSpecification provide() {
        return ServiceSpecification.builder()
                .method(GET_RESOURCE, ServiceMethodSpecification.builder()
                        .implementation(ServiceMethodImplementation.handler(path -> HttpResourceService.getHttpResource(cast(path)), "", ""))
                        .build())
                .build();
    }
}
