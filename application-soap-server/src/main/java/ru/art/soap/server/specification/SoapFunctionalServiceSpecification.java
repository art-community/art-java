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

package ru.art.soap.server.specification;

import lombok.*;
import ru.art.soap.server.model.*;
import java.util.function.*;

import static ru.art.core.caster.Caster.*;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.soap.server.constans.SoapServerModuleConstants.*;

@Getter
public class SoapFunctionalServiceSpecification implements SoapServiceSpecification {
    private final String serviceId;
    private final SoapService soapService;
    private final Function<?, ?> function;

    public SoapFunctionalServiceSpecification(SoapService soapService, Function<?, ?> function) {
        this.serviceId = SOAP_SERVICE_TYPE + OPENING_BRACKET + soapService.getPath() + CLOSING_BRACKET;
        this.soapService = soapService;
        this.function = function;
    }

    @Override
    public <P, R> R executeMethod(String methodId, P request) {
        return cast(function.apply(cast(request)));
    }
}