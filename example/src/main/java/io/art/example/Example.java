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

package io.art.example;

import io.art.model.annotation.*;
import io.art.model.implementation.*;
import io.art.model.modeler.*;
import static io.art.model.implementation.ModuleModel.*;

@Module
public class Example {
    @Modeler
    public static ModuleModel model() {
        return module()
                .serve(server -> server
                        .rsocket(rsocket -> rsocket.to(Service.class, ServiceModeler::enableLogging)))
                .make();
    }
}
