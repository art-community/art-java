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

package ru.art.config;

import com.esotericsoftware.yamlbeans.YamlReader;
import io.advantageous.config.Config;
import static io.advantageous.config.ConfigLoader.loadFromObject;
import static java.lang.System.getProperty;
import static java.util.Objects.isNull;
import static ru.art.config.YamlConfigLoaderConstants.DEFAULT_YAML_CONFIG_FILE_NAME;
import static ru.art.config.YamlLoadingExceptionMessages.CONFIG_FILE_WAS_NOT_FOUND;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.core.constants.SystemProperties.CONFIG_FILE_PATH_PROPERTY;
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
        String configFilePath = getProperty(CONFIG_FILE_PATH_PROPERTY);
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