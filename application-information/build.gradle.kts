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
        applicationService()
        applicationLogging()
        applicationGrpcServer()
        applicationHttp()
        applicationJson()
        applicationHttpServer()
        applicationRsocket()
    }
    generator {
        packageName = "ru.art.information"
    }
}

dependencies {
    embedded("org.jeasy", "easy-random", "4.+")
            .exclude("com.fasterxml.jackson.core")
    embedded("org.jeasy", "easy-random-core", "4.+")
            .exclude("com.fasterxml.jackson.core")
    embedded("com.fasterxml.jackson.core", "jackson-core", art.externalDependencyVersionsConfiguration.jacksonVersion)
    embedded("com.fasterxml.jackson.core", "jackson-databind", art.externalDependencyVersionsConfiguration.jacksonVersion)
}