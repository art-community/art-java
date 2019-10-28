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

import org.gradle.api.JavaVersion.*
import org.jetbrains.kotlin.gradle.tasks.*
import org.gradle.internal.os.OperatingSystem as os

art {
    embeddedModules {
        kit()
    }
    web {
        buildToolCheckingCommand = if (os.current().isWindows) arrayOf("cmd", "/c", "yarn", "--no-lockfile", "--skip-integrity-check") else arrayOf("yarn", "--no-lockfile", "--skip-integrity-check")
        buildWebCommand = if (os.current().isWindows) listOf("cmd", "/c", "yarn", "run", "production") else listOf("yarn", "run", "production")
        prepareWebCommand = if (os.current().isWindows) listOf("cmd", "/c", "yarn") else listOf("yarn")
    }
    mainClass("ru.art.platform.module.ManagementPanelModule")
}

dependencies {
    embedded(project(":api"))
    embedded("com.auth0:java-jwt:3.8.+") {
        exclude("com.fasterxml.jackson.dataformat")
        exclude("com.fasterxml.jackson.core")
    }
    embedded("com.github.docker-java", "docker-java", "3.1.5")
}


tasks.withType<KotlinCompile> {
    sourceCompatibility = VERSION_11.toString()
    targetCompatibility = VERSION_11.toString()

    kotlinOptions {
        jvmTarget = VERSION_11.toString()
    }
}

tasks.withType(JavaCompile::class).forEach { task -> task.dependsOn(":api:build") }