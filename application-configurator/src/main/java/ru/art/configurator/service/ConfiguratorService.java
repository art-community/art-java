package ru.art.configurator.service;

import ru.art.config.remote.api.specification.RemoteConfigCommunicationSpecification;
import ru.art.configurator.api.entity.Configuration;
import ru.art.configurator.api.entity.ModuleConfiguration;
import ru.art.configurator.api.entity.ModuleKey;
import ru.art.configurator.api.entity.ProfileConfiguration;
import ru.art.core.checker.CheckerForEmptiness;
import ru.art.entity.Value;
import static ru.art.configurator.api.entity.Configuration.ConfigurationBuilder;
import static ru.art.configurator.api.entity.Configuration.builder;
import static ru.art.configurator.constants.ConfiguratorDbConstants.APPLICATION;
import static ru.art.configurator.dao.ConfiguratorDao.*;
import static ru.art.configurator.factory.RemoteConfigProxyServiceSpecificationsFactory.createRemoteConfigProxySpecs;
import static ru.art.configurator.provider.ApplicationModulesParametersProvider.getApplicationModuleParameters;
import static ru.art.entity.Entity.entityBuilder;
import java.util.Set;


public interface ConfiguratorService {
    static void uploadConfiguration(Configuration configuration) {
        saveConfig(configuration);
    }

    static void uploadApplicationConfiguration(Configuration configuration) {
        saveApplicationConfiguration(configuration.getConfiguration());
    }

    static void uploadProfileConfiguration(ProfileConfiguration configuration) {
        saveProfileConfiguration(configuration.getProfileId(), configuration.getConfiguration());
    }

    static void uploadModuleConfiguration(ModuleConfiguration configuration) {
        saveModuleConfiguration(configuration.getModuleKey(), configuration.getConfiguration());
    }


    static Configuration getConfiguration(ModuleKey moduleKey) {
        ConfigurationBuilder applicationConfigurationBuilder = builder().configuration(entityBuilder().build());
        getConfig(moduleKey.formatKey())
                .map(Value::asEntity)
                .ifPresent(applicationConfigurationBuilder::configuration);
        return applicationConfigurationBuilder.build();
    }

    static Set<ModuleKey> getProfiles() {
        return getProfileKeys();
    }

    static Set<ModuleKey> getModules() {
        return getModuleKeys();
    }

    static Configuration getApplicationConfiguration() {
        return builder()
                .configuration(getConfig(APPLICATION).map(Value::asEntity).orElse(entityBuilder().build()))
                .build();
    }


    static void applyModuleConfiguration(ModuleKey moduleKey) {
        getApplicationModuleParameters(moduleKey)
                .filter(CheckerForEmptiness::isNotEmpty)
                .map(parameters -> createRemoteConfigProxySpecs(parameters, moduleKey))
                .ifPresent(specs -> specs.forEach(RemoteConfigCommunicationSpecification::applyConfiguration));
    }
}