package ru.adk.configurator.factory;

import lombok.SneakyThrows;
import org.apache.http.client.utils.URIBuilder;
import ru.adk.config.remote.api.specification.RemoteConfigCommunicationSpecification;
import ru.adk.configurator.api.entity.ModuleKey;
import ru.adk.configurator.provider.ApplicationModulesParametersProvider.ApplicationModuleParameters;
import static java.lang.Integer.valueOf;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toSet;
import static ru.adk.configurator.constants.ConfiguratorModuleConstants.GRPC_INSTANCES;
import static ru.adk.core.constants.StringConstants.*;
import static ru.adk.entity.Value.asCollection;
import static ru.adk.http.client.communicator.HttpCommunicator.httpCommunicator;
import static ru.adk.http.constants.HttpCommonConstants.HTTP_SCHEME;
import static ru.adk.http.constants.MimeToContentTypeMapper.applicationJsonUtf8;
import java.util.Set;

public interface RemoteConfigProxyServiceSpecificationsFactory {
    @SneakyThrows
    static Set<RemoteConfigCommunicationSpecification> createRemoteConfigProxySpecs(ApplicationModuleParameters parameters, ModuleKey moduleKey) {
        URIBuilder uriBuilder = new URIBuilder();
        String url = uriBuilder
                .setScheme(HTTP_SCHEME)
                .setHost(parameters.getBalancerHost())
                .setPort(parameters.getBalancerPort())
                .setPath(moduleKey.getModuleId().replaceAll(DASH, UNDERSCORE) + DOT + moduleKey.getProfileId().replaceAll(DASH, UNDERSCORE) + GRPC_INSTANCES)
                .build()
                .toString();
        return httpCommunicator(url)
                .responseMapper(hosts -> asCollection(hosts).getStringSet())
                .consumes(applicationJsonUtf8())
                .get()
                .<Set<String>>execute()
                .orElse(emptySet())
                .stream()
                .map(host -> new RemoteConfigCommunicationSpecification(host.split(COLON)[0], valueOf(host.split(COLON)[1]), parameters.getPath()))
                .collect(toSet());
    }
}
