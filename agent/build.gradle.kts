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
    mainClass("ru.art.platform.agent.module.AgentModule")
}


dependencies {
    embedded(project(":api"))
    embedded("org.zeroturnaround", "zt-exec", art.externalDependencyVersionsConfiguration.zeroTurnaroundVersion)
    embedded("org.eclipse.jgit:org.eclipse.jgit:5.5.1.201910021850-r")
}

task("buildDockerImage", type = Exec::class) {
    if (os.current().isWindows) {
        commandLine("cmd", "/c", "docker build --rm -t platform/agent:latest .")
        return@task
    }
    commandLine("docker build --rm -t platform/agent:latest .")
}

tasks["build"].finalizedBy("buildDockerImage")

tasks.withType<KotlinCompile> {
    sourceCompatibility = VERSION_11.toString()
    targetCompatibility = VERSION_11.toString()

    kotlinOptions {
        jvmTarget = VERSION_11.toString()
    }
}

tasks.withType(JavaCompile::class).forEach { task -> task.dependsOn(":api:build") }