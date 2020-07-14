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

import com.jfrog.bintray.gradle.BintrayExtension.PackageConfig
import com.jfrog.bintray.gradle.tasks.BintrayUploadTask
import groovy.lang.GroovyObject
import org.jfrog.gradle.plugin.artifactory.dsl.PublisherConfig
import org.jfrog.gradle.plugin.artifactory.task.ArtifactoryTask

plugins {
    `maven-publish`
    idea
    `java-library`
    id("com.jfrog.bintray") version "1.8.4"
    id("com.jfrog.artifactory") version "4.10.0"
}

tasks.withType(Wrapper::class.java) {
    gradleVersion = "6.5.1"
}

val bintrayUser: String? by project
val bintrayKey: String? by project
val version: String? by project

group = "io.github.art"

buildScan {
    termsOfServiceUrl = "https://gradle.com/terms-of-service"
    termsOfServiceAgree = "yes"
}

subprojects {
    group = rootProject.group

    repositories {
        jcenter()
        mavenCentral()
        maven {
            url = uri("https://oss.jfrog.org/oss-snapshot-local")
        }
    }

    apply(plugin = "com.jfrog.bintray")
    apply(plugin = "com.jfrog.artifactory")
    apply(plugin = "maven-publish")
    apply(plugin = "java-library")

    if (bintrayUser.isNullOrEmpty() || bintrayKey.isNullOrEmpty()) {
        return@subprojects
    }

    val jar: Jar by tasks
    val sourceJar = task("sourceJar", type = Jar::class) {
        archiveClassifier.set("sources")
        from(sourceSets.main.get().allJava)
    }

    publishing {
        publications {
            create<MavenPublication>(project.name) {
                artifact(jar)
                artifact(sourceJar)
                groupId = rootProject.group as String
                artifactId = project.name
                version = rootProject.version as String
                pom {
                    packaging = "jar"
                    description.set(project.name)
                }
            }
        }
    }

    artifactory {
        setContextUrl("https://oss.jfrog.org")
        publish(delegateClosureOf<PublisherConfig> {
            repository(delegateClosureOf<GroovyObject> {
                setProperty("repoKey", "oss-snapshot-local")
                setProperty("username", bintrayUser ?: "")
                setProperty("password", bintrayKey ?: "")
                setProperty("maven", true)
            })
        })
        val artifactoryPublish: ArtifactoryTask by tasks
        with(artifactoryPublish) {
            publications(project.name)
        }
        artifactoryPublish.dependsOn(tasks["generatePomFileFor${name.capitalize()}Publication"], jar, sourceJar)
    }

    bintray {
        user = bintrayUser ?: ""
        key = bintrayKey ?: ""
        publish = true
        override = true
        setPublications(project.name)
        pkg(delegateClosureOf<PackageConfig> {
            repo = "art"
            name = rootProject.group as String
            userOrg = "art-community"
            websiteUrl = "art-platform.io"
            vcsUrl = "https://github.com/art-community/art-java"
            setLabels("art")
            setLicenses("Apache-2.0")
        })

        with(tasks["bintrayUpload"] as BintrayUploadTask) {
            publish = true
            dependsOn(tasks["generatePomFileFor${this@subprojects.name.capitalize()}Publication"], jar, sourceJar)
        }

    }
}

afterEvaluate {
    tasks["bintrayUpload"].enabled = false
    tasks["bintrayPublish"].enabled = false
    tasks["artifactoryPublish"].enabled = false
}
