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

package io.art.http.server.function;

import lombok.*;
import io.art.http.server.model.*;
import io.art.http.server.specification.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.http.server.constants.HttpServerModuleConstants.*;
import java.util.function.*;

@Getter
public class HttpFunctionalServiceSpecification implements HttpServiceSpecification {
    private final String serviceId;
    private final HttpService httpService;
    private final Function<?, ?> function;

    HttpFunctionalServiceSpecification(HttpService httpService, Function<?, ?> function) {
        this.serviceId = HTTP_SERVICE_TYPE + OPENING_BRACKET + httpService.getHttpMethods().get(0).getPath() + CLOSING_BRACKET;
        this.httpService = httpService;
        this.function = function;
    }

    @Override
    public <P, R> R executeMethod(String methodId, P request) {
        return cast(function.apply(cast(request)));
    }
}
