import org.gradle.internal.jvm.Jvm

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
        applicationLogging()
        applicationService()
        applicationHttp()
        applicationHttpClient()
        applicationXml()
    }
}

dependencies {
    with(art.externalDependencyVersionsConfiguration) {
        if (JavaVersion.current().isJava8) {
            embedded(files(Jvm.current().toolsJar))
        }
        embedded("com.squareup", "javapoet", javaPoetVersion)
        embedded("org.membrane-soa", "service-proxy-core", membraneSoaServiceProxyCoreVersion)
                .exclude("org.springframework")
                .exclude("com.fasterxml.jackson.core")
                .exclude("com.fasterxml.jackson.dataformat")
                .exclude("com.fasterxml.jackson.datatype")
                .exclude("com.github.fge")
                .exclude("commons-cli")
                .exclude("commons-codec")
                .exclude("commons-dbcp")
                .exclude("commons-discovery")
                .exclude("commons-fileupload")
                .exclude("javax.mail")
                .exclude("io.swagger")
                .exclude("com.floreysoft")
                .exclude("org.slf4j")
                .exclude("commons-logging")
                .exclude("org.apache.httpcomponents")
                .exclude("com.google.guava")
    }
}
