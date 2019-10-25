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

import com.esotericsoftware.yamlbeans.*;
import static java.lang.System.*;
import static java.util.Objects.*;
import static ru.art.config.YamlConfigLoaderConstants.*;
import static ru.art.config.YamlLoadingExceptionMessages.*;
import static ru.art.core.caster.Caster.*;
import static ru.art.core.checker.CheckerForEmptiness.*;
import static ru.art.core.constants.SystemProperties.*;
import static ru.art.core.context.Context.*;
import static ru.art.core.finder.MapEntryFinder.find;
import static ru.art.core.wrapper.ExceptionWrapper.*;
import java.io.*;
import java.net.*;
import java.util.*;

class YamlConfigLoader {
    static Map<String, ?> loadYamlConfig(String configId) {
        Reader reader = wrapException(() -> new BufferedReader(new InputStreamReader(loadConfigInputStream(), contextConfiguration().getCharset())), YamlLoadingException::new);
        Map<String, ?> configMap = wrapException(() -> cast(new YamlReader(reader).read()), YamlLoadingException::new);
        return isEmpty(configId) ? configMap : cast(find(configMap, configId));
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