/*
 * ART
 *
 * Copyright 2020 ART
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



        project(":Core()
        project(":Entity()
        project(":Logging()
        project(":Service()
        project(":TemplateEngine()
    }

    resources {
        resourceDirs.add("src/main/templates")
        resourceDirs.add("src/main/executable")
        resourceDirs.add("src/main/lua")
    }
}

dependencies {

        api("org.zeroturnaround", "zt-exec", zeroTurnaroundVersion)
                .exclude("org.slf4j")
        api("org.tarantool", "connector", tarantoolConnectorVersion)
                .exclude("org.slf4j")
        api("org.apache.logging.log4j", "log4j-iostreams", log4jVersion)
                .exclude("org.apache.logging.log4j")
    }
}
