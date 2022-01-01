/*
 * ART
 *
 * Copyright 2019-2022 ART
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

plugins {
    id("art-internal-jvm")
    id("me.champeau.jmh")
}

dependencies {
    api(project(":core"))
    api(project(":meta"))
    api(project(":transport"))
    api(project(":server"))
    api(project(":communicator"))

    implementation(project(":logging"))

    val rsocketVersion: String by project
    api("io.rsocket", "rsocket-transport-netty", rsocketVersion)
            .exclude("io.zipkin.brave")
            .exclude("io.netty")
            .exclude("io.projectreactor", "reactor-core")
            .exclude("org.slf4j")

    testImplementation(project(":message-pack"))
    testImplementation(testFixtures(project(":meta")))
}

generator {
    source("Rsocket") {
        modulePackage("io.art.rsocket")
        jvm()
        sourcesPattern {
            include("src/main/**")
        }
        includeClasses("**model**")
    }
    source("RsocketTest") {
        modulePackage("io.art.rsocket.test")
        jvm()
        sourcesPattern {
            include("src/test/**")
        }
        excludeClasses("**RsocketTest**")
    }
}
