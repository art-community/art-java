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

rootProject.name = "art-java"
include("core")
include("logging")
include("transport")
include("launcher")
include("configurator")
include("json")
include("yaml")
include("message-pack")
include("server")
include("communicator")
include("rsocket")
include("scheduler")
include("meta")
include("http")
include("tests")
include("tarantool")
include("storage")

pluginManagement {
    val internalPluginVersion: String by settings
    repositories {
        gradlePluginPortal()
        maven { url = uri("https://repo.repsy.io/mvn/antonsh/art-packages/") }
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id.contains("art")) {
                useModule("io.art.gradle:art-gradle:$internalPluginVersion")
            }
        }
    }
    plugins {
        val jmhVersion: String by settings
        id("me.champeau.jmh") version jmhVersion
        id("art-internal-jvm") version internalPluginVersion
    }
}
