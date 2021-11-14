/*
 * ART
 *
 * Copyright 2019-2021 ART
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
    `java-library`
    `java-test-fixtures`
    id("art-internal-jvm")
}

group = "io.art.java"

tasks.withType(type = Wrapper::class) {
    gradleVersion = "7.2"
}

generator {
    main {
        fileLogging()
    }
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    group = rootProject.group

    apply(plugin = "java-library")
    apply(plugin = "java-test-fixtures")

    afterEvaluate {
        tasks.findByName("jmhJar")?.let { task ->
            (task as? Jar)?.exclude("org/slf4j/impl/StaticLoggerBinder.class")
        }
    }

    dependencies {
        val lombokVersion: String by project
        val junitVersion: String by project

        compileOnly("org.projectlombok", "lombok", lombokVersion)
        annotationProcessor("org.projectlombok", "lombok", lombokVersion)
        testCompileOnly("org.projectlombok", "lombok", lombokVersion)
        testAnnotationProcessor("org.projectlombok", "lombok", lombokVersion)
        testFixturesCompileOnly("org.projectlombok", "lombok", lombokVersion)
        testFixturesAnnotationProcessor("org.projectlombok", "lombok", lombokVersion)

        testImplementation("org.junit.jupiter", "junit-jupiter-api", junitVersion)
        testRuntimeOnly("org.junit.jupiter", "junit-jupiter-engine", junitVersion)
        testFixturesImplementation("org.junit.jupiter", "junit-jupiter-api", junitVersion)
    }

    tasks.test {
        useJUnitPlatform()
    }

    java {
        withSourcesJar()
    }
}
