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

package io.art.launcher;

import io.art.model.module.*;
import io.art.model.server.*;
import io.art.server.model.*;
import io.art.server.specification.*;
import lombok.experimental.*;
import static io.art.server.module.ServerModule.*;
import java.util.*;

@UtilityClass
public class ModelImplementor {
    public static void implement(ModuleModel model) {
        ServerModel serverModel = model.getServerModel();
        Map<ServiceMethodIdentifier, ServiceMethodSpecification> rsocketMethods = serverModel.getRsocketMethodsModel();
        for (ServiceMethodSpecification specification : rsocketMethods.values()) {
            serverModule().state().getSpecifications().register(specification.getServiceSpecification());
        }
    }
}
