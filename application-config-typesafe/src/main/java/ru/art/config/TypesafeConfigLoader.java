/*
 * ART Java
 *
 * Copyright 2019 ART
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
import static ru.art.core.constants.SystemProperties.CONFIG_FILE_PATH_PROPERTY;
import static ru.art.core.wrapper.ExceptionWrapper.wrapException;
import java.io.*;
import java.net.URL;

class TypesafeConfigLoader {
    static Config loadTypeSafeConfig(String configId, ConfigSyntax configSyntax) {
        ConfigParseOptions options = defaults().setSyntax(configSyntax == JSON ? JSON : CONF);
        com.typesafe.config.Config typeSafeConfig = parseReader(wrapException(() -> loadConfigReader(configSyntax), TypesafeConfigLoadingException::new), options);
        return fromTypeSafeConfig(typeSafeConfig).getConfig(configId);
    }

    private static Reader loadConfigReader(ConfigSyntax configSyntax) throws IOException {
        String configFilePath = getProperty(CONFIG_FILE_PATH_PROPERTY);
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