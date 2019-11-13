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

package ru.art.test.specification.configuration

import ru.art.config.constants.ConfigType

import static java.io.File.separator
import static ru.art.config.constants.ConfigType.*
import static ru.art.core.extension.FileExtensions.writeFile

class ModuleConfigGenerator {
    static String currentConfig
    static String currentConfigPath
    static private YAML_CONFIG =
            """
logging:
  level: trace
http:
  server:
    enableRawDataTracing: true
    port: 12
    path: myPath
    maxThreadsCount: 123
    minSpareThreadsCount: 456
    enableValueTracing: true
    enableMetrics: false
    consumesMimeType: image/gif
    producesMimeType: image/jpeg
    host: myHost
    web: false
            """
    static private PROPERTIES_CONFIG =
            """
logging.level=trace
http.server.enableRawDataTracing=true
http.server.port=12
http.server.path=myPath
http.server.maxThreadsCount=123
http.server.minSpareThreadsCount=456
http.server.enableValueTracing=true
http.server.enableMetrics=false
http.server.consumesMimeType=image/gif
http.server.producesMimeType=image/jpeg
http.server.host=myHost
http.server.web=false
            """
    static private JSON_CONFIG =
            """
{
  "logging": {
    "level": "trace"
  },
  "http": {
    "server": {
      "enableRawDataTracing": true,
      "port": 12,
      "path": "myPath",
      "maxThreadsCount": 123,
      "minSpareThreadsCount": 456,
      "enableValueTracing": true,
      "enableMetrics": false,
      "consumesMimeType": "image/gif",
      "producesMimeType": "image/jpeg",
      "host": "myHost",
      "web": false
    }
  }
}
            """

    static writeModuleConfig(ConfigType type) {
        String name
        String config
        switch (type) {
            case PROPERTIES:
                name = "module-config.properties"
                config = PROPERTIES_CONFIG
                break
            case JSON:
                name = "module-config.json"
                config = JSON_CONFIG
                break
            case HOCON:
                name = "module-config.hocon"
                break
            case YAML:
                name = "module-config.yml"
                config = YAML_CONFIG
                break
            default: return
        }
        File ymlConfigPath = [ConfigurationSpecification.class.classLoader.getResource("module-config.yml").file]
        if (type == YAML) {
            currentConfig = ymlConfigPath.text
            currentConfigPath = ymlConfigPath.absolutePath
        }
        writeFile(ymlConfigPath.parent + separator + name as String, config.toString())
    }

    static restoreConfig() {
        if (currentConfig && currentConfigPath) {
            writeFile(currentConfigPath, currentConfig)
        }
        currentConfig = ""
        currentConfigPath = ""
    }
}
