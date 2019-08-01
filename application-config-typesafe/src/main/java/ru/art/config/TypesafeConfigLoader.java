package ru.art.config;

import com.typesafe.config.ConfigParseOptions;
import com.typesafe.config.ConfigSyntax;
import io.advantageous.config.Config;
import static com.typesafe.config.ConfigFactory.parseReader;
import static com.typesafe.config.ConfigParseOptions.defaults;
import static com.typesafe.config.ConfigSyntax.CONF;
import static com.typesafe.config.ConfigSyntax.JSON;
import static io.advantageous.konf.typesafe.TypeSafeConfig.fromTypeSafeConfig;
import static java.lang.System.getProperty;
import static java.text.MessageFormat.format;
import static java.util.Objects.isNull;
import static ru.art.config.TypesafeConfigLoaderConstants.DEFAULT_TYPESAFE_CONFIG_FILE_NAME;
import static ru.art.config.TypesafeConfigLoadingExceptionMessages.CONFIG_FILE_NOT_FOUND;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.core.constants.SystemProperties.CONFIG_FILE_PATH;
import static ru.art.core.wrapper.ExceptionWrapper.wrap;
import java.io.*;
import java.net.URL;

class TypesafeConfigLoader {
    static Config loadTypeSafeConfig(String configId, ConfigSyntax configSyntax) {
        ConfigParseOptions options = defaults().setSyntax(configSyntax == JSON ? JSON : CONF);
        com.typesafe.config.Config typeSafeConfig = parseReader(wrap(() -> loadConfigReader(configSyntax), TypesafeConfigLoadingException::new), options);
        return fromTypeSafeConfig(typeSafeConfig).getConfig(configId);
    }

    private static Reader loadConfigReader(ConfigSyntax configSyntax) throws IOException {
        String configFilePath = getProperty(CONFIG_FILE_PATH);
        File configFile;
        if (isEmpty(configFilePath) || !(configFile = new File(configFilePath)).exists()) {
            URL configFileUrl = TypesafeConfigLoader.class.getClassLoader().getResource(DEFAULT_TYPESAFE_CONFIG_FILE_NAME);
            if (isNull(configFileUrl)) {
                throw new TypesafeConfigLoadingException(format(CONFIG_FILE_NOT_FOUND, configSyntax));
            }
            return new InputStreamReader(configFileUrl.openStream());
        }
        return new FileReader(configFile);
    }
}