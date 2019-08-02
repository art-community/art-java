/*
 *    Copyright 2019 ART
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package ru.art.grpc.server.specification;

import ru.art.grpc.server.constants.GrpcServerModuleConstants;
import ru.art.grpc.server.model.GrpcService;
import ru.art.service.Specification;
import static ru.art.core.factory.CollectionsFactory.fixedArrayOf;
import java.util.List;

public interface GrpcServiceSpecification extends Specification {
    GrpcService getGrpcService();

    @Override
    default List<String> getServiceTypes() {
        return fixedArrayOf(GrpcServerModuleConstants.GRPC_SERVICE_TYPE);
    }
}
