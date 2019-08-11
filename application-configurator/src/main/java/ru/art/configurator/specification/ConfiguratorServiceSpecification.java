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

package ru.art.configurator.specification;

import lombok.Getter;
import ru.art.configurator.api.entity.Configuration;
import ru.art.configurator.api.entity.ModuleConfiguration;
import ru.art.configurator.api.entity.ModuleKey;
import ru.art.configurator.api.entity.ProfileConfiguration;
import ru.art.grpc.server.model.GrpcService;
import ru.art.grpc.server.specification.GrpcServiceSpecification;
import ru.art.http.server.model.HttpService;
import ru.art.http.server.specification.HttpServiceSpecification;
import ru.art.service.exception.UnknownServiceMethodException;
import static ru.art.configurator.api.constants.ConfiguratorServiceConstants.CONFIGURATOR_SERVICE_ID;
import static ru.art.configurator.api.constants.ConfiguratorServiceConstants.Methods.*;
import static ru.art.configurator.api.mapping.ConfigurationMapping.configurationMapper;
import static ru.art.configurator.api.mapping.ModuleConfigurationMapping.moduleConfigurationMapper;
import static ru.art.configurator.api.mapping.ModuleKeyCollectionMapping.moduleKeyCollectionMapper;
import static ru.art.configurator.api.mapping.ModuleKeyMapping.moduleKeyMapper;
import static ru.art.configurator.api.mapping.ProfileConfigurationMapping.profileConfigurationMapper;
import static ru.art.configurator.constants.ConfiguratorModuleConstants.*;
import static ru.art.configurator.service.ConfiguratorService.*;
import static ru.art.core.caster.Caster.cast;
import static ru.art.core.factory.CollectionsFactory.fixedArrayOf;
import static ru.art.grpc.server.constants.GrpcServerModuleConstants.GRPC_SERVICE_TYPE;
import static ru.art.grpc.server.model.GrpcService.GrpcMethod.grpcMethod;
import static ru.art.grpc.server.model.GrpcService.grpcService;
import static ru.art.http.constants.MimeToContentTypeMapper.applicationJsonUtf8;
import static ru.art.http.server.constants.HttpServerModuleConstants.HTTP_SERVICE_TYPE;
import static ru.art.http.server.model.HttpService.httpService;
import static ru.art.http.server.module.HttpServerModule.httpServerModule;
import static ru.art.service.constants.RequestValidationPolicy.NOT_NULL;
import static ru.art.service.constants.RequestValidationPolicy.VALIDATABLE;
import java.util.List;

@Getter
public class ConfiguratorServiceSpecification implements HttpServiceSpecification, GrpcServiceSpecification {
    private final String serviceId = CONFIGURATOR_SERVICE_ID;

    private final HttpService httpService = httpService()
            .post(UPLOAD_CONFIG)
            .consumes(applicationJsonUtf8())
            .fromBody()
            .validationPolicy(VALIDATABLE)
            .requestMapper(configurationMapper.getToModel())
            .produces(applicationJsonUtf8())
            .listen(UPLOAD_PATH)

            .post(GET_JSON_CONFIG)
            .fromBody()
            .validationPolicy(NOT_NULL)
            .requestMapper(moduleKeyMapper.getToModel())
            .produces(applicationJsonUtf8())
            .responseMapper(configurationMapper.getFromModel())
            .listen(GET_PATH)

            .post(GET_APPLICATION_CONFIG)
            .produces(applicationJsonUtf8())
            .responseMapper(configurationMapper.getFromModel())
            .listen(GET_APPLICATION_CONFIGURATION_PATH)

            .post(APPLY_MODULE_CONFIG)
            .consumes(applicationJsonUtf8())
            .fromBody()
            .validationPolicy(NOT_NULL)
            .requestMapper(moduleKeyMapper.getToModel())
            .listen(APPLY_PATH)

            .post(GET_ALL_PROFILES)
            .produces(applicationJsonUtf8())
            .responseMapper(moduleKeyCollectionMapper.getFromModel())
            .listen(PROFILES_PATH)

            .post(GET_ALL_MODULES)
            .consumes(applicationJsonUtf8())
            .fromBody()
            .requestMapper(moduleKeyMapper.getToModel())
            .produces(applicationJsonUtf8())
            .responseMapper(moduleKeyCollectionMapper.getFromModel())
            .listen(MODULES_PATH)

            .post(UPLOAD_APPLICATION_CONFIG)
            .consumes(applicationJsonUtf8())
            .fromBody()
            .validationPolicy(VALIDATABLE)
            .requestMapper(configurationMapper.getToModel())
            .produces(applicationJsonUtf8())
            .listen(UPLOAD_APPLICATION_PATH)

            .post(UPLOAD_PROFILE_CONFIG)
            .consumes(applicationJsonUtf8())
            .fromBody()
            .requestMapper(profileConfigurationMapper.getToModel())
            .produces(applicationJsonUtf8())
            .listen(UPLOAD_PROFILE_PATH)

            .post(UPLOAD_MODULE_CONFIG)
            .consumes(applicationJsonUtf8())
            .fromBody()
            .requestMapper(moduleConfigurationMapper.getToModel())
            .produces(applicationJsonUtf8())
            .listen(UPLOAD_MODULE_PATH)

            .serve(httpServerModule().getPath());

    private final GrpcService grpcService = grpcService()
            .method(GET_PROTOBUF_CONFIG, grpcMethod()
                    .requestMapper(moduleKeyMapper.getToModel())
                    .responseMapper(configurationMapper.getFromModel()))
            .serve();

    private final List<String> serviceTypes = fixedArrayOf(GRPC_SERVICE_TYPE, HTTP_SERVICE_TYPE);

    @Override
    public <P, R> R executeMethod(String methodId, P request) {
        switch (methodId) {
            case UPLOAD_CONFIG:
                uploadConfiguration((Configuration) request);
                return null;
            case UPLOAD_APPLICATION_CONFIG:
                uploadApplicationConfiguration((Configuration) request);
                return null;
            case UPLOAD_PROFILE_CONFIG:
                uploadProfileConfiguration((ProfileConfiguration) request);
                return null;
            case UPLOAD_MODULE_CONFIG:
                uploadModuleConfiguration((ModuleConfiguration) request);
                return null;
            case GET_JSON_CONFIG:
            case GET_PROTOBUF_CONFIG:
                return cast(getConfiguration((ModuleKey) request));
            case APPLY_MODULE_CONFIG:
                applyModuleConfiguration((ModuleKey) request);
                return null;
            case GET_ALL_PROFILES:
                return cast(getProfiles());
            case GET_ALL_MODULES:
                return cast(getModules());
            case GET_APPLICATION_CONFIG:
                return cast(getApplicationConfiguration());
            default:
                throw new UnknownServiceMethodException(serviceId, methodId);
        }
    }
}
