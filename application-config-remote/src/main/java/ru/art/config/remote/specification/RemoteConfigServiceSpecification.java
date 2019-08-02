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

package ru.art.config.remote.specification;

import lombok.Getter;
import lombok.ToString;
import ru.art.grpc.server.model.GrpcService;
import ru.art.grpc.server.specification.GrpcServiceSpecification;
import ru.art.service.exception.UnknownServiceMethodException;
import static ru.art.config.remote.api.constants.RemoteConfigApiConstants.Methods.APPLY_CONFIGURATION_METHOD_ID;
import static ru.art.config.remote.api.constants.RemoteConfigApiConstants.REMOTE_CONFIG_SERVICE_ID;
import static ru.art.config.remote.service.RemoteConfigService.applyConfiguration;
import static ru.art.core.factory.CollectionsFactory.fixedArrayOf;
import static ru.art.grpc.server.constants.GrpcServerModuleConstants.GRPC_SERVICE_TYPE;
import static ru.art.grpc.server.model.GrpcService.GrpcMethod.grpcMethod;
import static ru.art.grpc.server.model.GrpcService.grpcService;
import java.util.List;

@ToString
@Getter
public class RemoteConfigServiceSpecification implements GrpcServiceSpecification {
    private final String serviceId = REMOTE_CONFIG_SERVICE_ID;
    private final GrpcService grpcService = grpcService().method(APPLY_CONFIGURATION_METHOD_ID, grpcMethod()).serve();
    private final List<String> serviceTypes = fixedArrayOf(GRPC_SERVICE_TYPE);

    @Override
    public <P, R> R executeMethod(String methodId, P request) {
        if (APPLY_CONFIGURATION_METHOD_ID.equals(methodId)) {
            applyConfiguration();
            return null;
        }
        throw new UnknownServiceMethodException(serviceId, methodId);
    }
}
