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


plugins {
    idea
    `java-library`
}

group = "io.art"


subprojects {
    group = rootProject.group

    repositories {
        jcenter()
        mavenCentral()
        maven {
            url = uri("https://repo.spring.io/milestone")
        }
    }

    apply(plugin = "java")
    apply(plugin = "java-library")

    dependencies {
        val lombokVersion: String by project
        compileOnly("org.projectlombok", "lombok", lombokVersion)
        annotationProcessor("org.projectlombok", "lombok", lombokVersion)
    }

    tasks.findByPath("check")?.let { check ->
        check.setDependsOn(check.dependsOn
                .filter { task ->
                    when (task) {
                        is String -> task != "test"
                        is TaskProvider<*> -> task.name != "test"
                        else -> true
                    }
                }
                .toSet())
    }

    tasks.findByPath("build")?.let { check ->
        check.setDependsOn(check.dependsOn
                .filter { task ->
                    when (task) {
                        is String -> task != "test"
                        is TaskProvider<*> -> task.name != "test"
                        else -> true
                    }
                }
                .toSet())
    }
}
