package ru.adk.configurator.api.specification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.adk.configurator.api.entity.Configuration;
import ru.adk.configurator.api.entity.ModuleKey;
import ru.adk.grpc.client.communicator.GrpcCommunicator;
import ru.adk.grpc.client.specification.GrpcCommunicationSpecification;
import ru.adk.service.exception.UnknownServiceMethodException;
import static java.lang.System.getProperty;
import static ru.adk.configurator.api.constants.ConfiguratorProxyServiceConstants.CONFIGURATOR_COMMUNICATION_SERVICE_ID;
import static ru.adk.configurator.api.constants.ConfiguratorServiceConstants.CONFIGURATOR_PATH;
import static ru.adk.configurator.api.constants.ConfiguratorServiceConstants.CONFIGURATOR_SERVICE_ID;
import static ru.adk.configurator.api.constants.ConfiguratorServiceConstants.Methods.GET_PROTOBUF_CONFIG;
import static ru.adk.configurator.api.mapping.ConfigurationMapping.configurationMapper;
import static ru.adk.configurator.api.mapping.ModuleKeyMapping.moduleKeyMapper;
import static ru.adk.core.caster.Caster.cast;
import static ru.adk.core.checker.CheckerForEmptiness.isEmpty;
import static ru.adk.core.constants.ContextConstants.LOCAL_PROFILE;
import static ru.adk.core.constants.SystemProperties.PROFILE_PROPERTY;
import static ru.adk.core.context.Context.contextConfiguration;
import static ru.adk.core.extension.NullCheckingExtensions.getOrElse;
import static ru.adk.entity.Entity.entityBuilder;
import static ru.adk.grpc.client.communicator.GrpcCommunicator.grpcCommunicator;

@Getter
@AllArgsConstructor
public class ConfiguratorProxyServiceSpecification implements GrpcCommunicationSpecification {
    private final String host;
    private final Integer port;
    private final String path = CONFIGURATOR_PATH;
    private final String serviceId = CONFIGURATOR_COMMUNICATION_SERVICE_ID;
    private final String profileId = getProperty(PROFILE_PROPERTY);
    private final ModuleKey moduleKey = new ModuleKey(isEmpty(profileId) ? LOCAL_PROFILE : profileId, contextConfiguration().getMainModuleId());

    @Getter(lazy = true)
    private final GrpcCommunicator getProtobufConfig = grpcCommunicator(host, port, path)
            .serviceId(CONFIGURATOR_SERVICE_ID)
            .methodId(GET_PROTOBUF_CONFIG)
            .requestMapper(moduleKeyMapper.getFromModel())
            .responseMapper(configurationMapper.getToModel());

    private Configuration getConfiguration() {
        try {
            return getOrElse((Configuration) getGetProtobufConfig().execute(moduleKey).getResponseData(), Configuration.builder().build());
        } catch (Exception e) {
            return Configuration.builder()
                    .configuration(entityBuilder().build())
                    .build();
        }
    }

    @Override
    public <P, R> R executeMethod(String methodId, P request) {
        if (GET_PROTOBUF_CONFIG.equals(methodId)) {
            return cast(getConfiguration());
        }
        throw new UnknownServiceMethodException(getServiceId(), methodId);
    }
}

