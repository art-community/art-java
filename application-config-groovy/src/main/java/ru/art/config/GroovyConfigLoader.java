package ru.art.config;

import groovy.util.ConfigObject;
import groovy.util.ConfigSlurper;
import static java.lang.System.getProperty;
import static java.util.Objects.isNull;
import static ru.art.config.GroovyConfigLoaderConstants.DEFAULT_GROOVY_CONFIG_FILE_NAME;
import static ru.art.config.GroovyConfigLoadingExceptionMessages.CONFIG_FILE_NOT_FOUND;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.core.constants.SystemProperties.CONFIG_FILE_PATH;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

interface GroovyConfigLoader {
    static ConfigObject loadGroovyConfig(String configId) {
        String configFilePath = getProperty(CONFIG_FILE_PATH);
        File configFile;
        URL configFileUrl;
        if (isEmpty(configFilePath) || !(configFile = new File(configFilePath)).exists()) {
            configFileUrl = GroovyConfigLoader.class.getClassLoader().getResource(DEFAULT_GROOVY_CONFIG_FILE_NAME);
            if (isNull(configFileUrl)) {
                throw new GroovyConfigLoadingException(CONFIG_FILE_NOT_FOUND);
            }
            return (ConfigObject) new ConfigSlurper().parse(configFileUrl).get(configId);
        }
        try {
            configFileUrl = configFile.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new GroovyConfigLoadingException(CONFIG_FILE_NOT_FOUND);
        }
        return (ConfigObject) new ConfigSlurper().parse(configFileUrl).get(configId);
    }
}