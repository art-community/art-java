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

package ru.art.module.executor.model;

import lombok.*;
import ru.art.entity.Value;
import ru.art.service.validation.*;

import static ru.art.service.validation.ValidationExpressions.*;

/**
 * Request with information about calling module
 */

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class ExecutionRequest implements Validatable {
    private String moduleHost;
    @Builder.Default
    private final int modulePort = 80; //default port
    private String executableServletPath;
    private String executableServiceId;
    private String executableMethodId;
    private Value executableRequest;

    @Override
    public void onValidating(Validator validator) {
        validator.validate("moduleHost", moduleHost, notEmptyString());
        validator.validate("executableServletPath", executableServletPath, notEmptyString());
        validator.validate("executableServiceId", executableServiceId, notEmptyString());
        validator.validate("executableMethodId", executableMethodId, notEmptyString());
    }
}

