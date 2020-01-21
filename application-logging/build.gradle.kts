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

art {
    providedModules {
        applicationCore()
        applicationEntity()
    }
}

dependencies {
    with(art.externalDependencyVersionsConfiguration) {
        embedded("org.slf4j", "slf4j-api", sl4jVersion)
        embedded("org.apache.logging.log4j", "log4j-slf4j-impl", log4jVersion)

        embedded("commons-logging", "commons-logging", commonsLoggingVersion)
        embedded("org.apache.logging.log4j", "log4j-jcl", log4jVersion)

        embedded("org.apache.logging.log4j", "log4j-api", log4jVersion)
        embedded("org.apache.logging.log4j", "log4j-core", log4jVersion)

        embedded("org.apache.logging.log4j", "log4j-jul", log4jVersion)
        embedded("com.lmax", "disruptor", "3.4.+")

        embedded("com.fasterxml.jackson.dataformat", "jackson-dataformat-yaml", jacksonVersion)
        embedded("com.fasterxml.jackson.core", "jackson-core", jacksonVersion)
        embedded("com.fasterxml.jackson.core", "jackson-databind", jacksonVersion)
    }
}