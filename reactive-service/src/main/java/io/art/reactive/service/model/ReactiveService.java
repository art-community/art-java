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

package io.art.reactive.service.model;

import lombok.*;
import lombok.experimental.*;
import io.art.reactive.service.wrapper.*;
import static io.art.reactive.service.constants.ReactiveServiceModuleConstants.*;
import static io.art.reactive.service.constants.ReactiveServiceModuleConstants.ReactiveMethodProcessingMode.*;
import static io.art.reactive.service.module.ReactiveServiceModule.*;
import java.util.*;

@Getter
@Builder(builderMethodName = "reactiveService", buildMethodName = "serve")
public class ReactiveService {
    @Singular("method")
    private final Map<String, ReactiveMethod> methods;

    @Getter
    @Setter
    @Accessors(fluent = true)
    @NoArgsConstructor(staticName = "reactiveMethod")
    public static class ReactiveMethod {
        private ReactiveMethodProcessingMode requestProcessingMode = STRAIGHT;
        private ReactiveMethodProcessingMode responseProcessingMode = STRAIGHT;
        private ReactiveServiceExceptionWrappers reactiveServiceExceptionWrappers = reactiveServiceModule().getReactiveServiceExceptionWrappers();
    }
}
