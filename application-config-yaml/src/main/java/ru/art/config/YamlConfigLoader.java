package ru.art.config;

import com.esotericsoftware.yamlbeans.YamlReader;
import io.advantageous.config.Config;
import static io.advantageous.config.ConfigLoader.loadFromObject;
import static java.lang.System.getProperty;
import static java.util.Objects.isNull;
import static ru.art.config.YamlConfigLoaderConstants.DEFAULT_YAML_CONFIG_FILE_NAME;
import static ru.art.config.YamlLoadingExceptionMessages.CONFIG_FILE_WAS_NOT_FOUND;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.core.constants.SystemProperties.CONFIG_FILE_PATH;
import static ru.art.core.context.Context.contextConfiguration;
import static ru.art.core.wrapper.ExceptionWrapper.wrap;
import java.io.*;
import java.net.URL;

class YamlConfigLoader {
    static Config loadYamlConfig(String configId) {
        Reader reader = wrap(() -> new BufferedReader(new InputStreamReader(loadConfigInputStream(), contextConfiguration().getCharset())), YamlLoadingException::new);
        return loadFromObject(wrap(() -> new YamlReader(reader).read(), YamlLoadingException::new)).getConfig(configId);
    }

    private static InputStream loadConfigInputStream() throws IOException {
        String configFilePath = getProperty(CONFIG_FILE_PATH);
        File configFile;
        if (isEmpty(configFilePath) || !(configFile = new File(configFilePath)).exists()) {
            URL configFileUrl = YamlConfigLoader.class.getClassLoader().getResource(DEFAULT_YAML_CONFIG_FILE_NAME);
            if (isNull(configFileUrl)) {
                throw new YamlLoadingException(CONFIG_FILE_WAS_NOT_FOUND);
            }
            return configFileUrl.openStream();
        }
        return new FileInputStream(configFile);
    }
}