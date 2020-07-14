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

package io.art.soap.server.specification;

import lombok.*;
import io.art.soap.server.model.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.soap.server.constans.SoapServerModuleConstants.*;
import java.util.function.*;

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
