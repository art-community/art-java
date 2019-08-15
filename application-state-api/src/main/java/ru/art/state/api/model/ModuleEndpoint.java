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

package ru.art.state.api.model;

import lombok.*;
import ru.art.service.validation.Validatable;
import ru.art.service.validation.Validator;
import static ru.art.service.validation.ValidationExpressions.moreThanInt;
import static ru.art.service.validation.ValidationExpressions.notEmptyString;

@Getter
@Builder
@ToString
@EqualsAndHashCode(of = {"host", "port"})
public class ModuleEndpoint implements Validatable {
    private final String host;
    private final Integer port;

    @Setter
    @Builder.Default
    @SuppressWarnings("UnusedAssignment")
    private Integer sessions = 0;

    @Override
    public void onValidating(Validator validator) {
        validator.validate("host", host, notEmptyString()).validate("port", port, moreThanInt(0));
    }
}
