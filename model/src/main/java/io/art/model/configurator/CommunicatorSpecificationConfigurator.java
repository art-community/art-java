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

package io.art.model.configurator;

import io.art.communicator.specification.CommunicatorSpecification.*;
import io.art.model.implementation.*;
import io.art.rsocket.communicator.*;
import lombok.*;
import static io.art.rsocket.module.RsocketModule.*;
import static lombok.AccessLevel.*;
import java.util.function.*;

@Getter(value = PACKAGE)
@RequiredArgsConstructor(access = PACKAGE)
public class CommunicatorSpecificationConfigurator {
    private final String id;
    private final Class<?> implementationInterface;
    private final Function<CommunicatorSpecificationBuilder, CommunicatorSpecificationBuilder> decorator = builder -> builder.implementation(RsocketCommunicatorImplementation.builder()
            .client(rsocketModule().state().getClient(getId()))
            .build());

    CommunicatorSpecificationModel configure() {
        return new CommunicatorSpecificationModel(id, implementationInterface, decorator);
    }
}
