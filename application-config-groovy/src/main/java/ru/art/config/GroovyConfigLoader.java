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

import groovy.util.*;
import static java.lang.System.*;
import static java.nio.file.Paths.*;
import static java.util.Objects.*;
import static ru.art.config.GroovyConfigLoaderConstants.*;
import static ru.art.config.GroovyConfigLoadingExceptionMessages.*;
import static ru.art.core.checker.CheckerForEmptiness.*;
import static ru.art.core.constants.SystemProperties.*;
import static ru.art.core.extension.FileExtensions.*;
import java.io.*;
import java.net.*;

interface GroovyConfigLoader {
    static ConfigObject loadGroovyConfig(String configId) {
        String configFilePath = getProperty(CONFIG_FILE_PATH_PROPERTY);
        File configFile;
        URL configFileUrl;
        if (isEmpty(configFilePath) || !(configFile = new File(configFilePath)).exists() || isEmpty(readFile(get(configFile.getAbsolutePath())))) {
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