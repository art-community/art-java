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

import com.jfrog.bintray.gradle.BintrayExtension.PackageConfig
import groovy.lang.GroovyObject
import org.jfrog.gradle.plugin.artifactory.dsl.PublisherConfig
import org.jfrog.gradle.plugin.artifactory.task.ArtifactoryTask

plugins {
    `maven-publish`
    id("com.gradle.build-scan") version "2.0.2"
    id("io.github.art.project") version "1.0.66"
    id("com.jfrog.bintray") version "1.8.4"
    id("com.jfrog.artifactory") version "4.9.8"
}

tasks.withType(Wrapper::class.java) {
    gradleVersion = "5.6"
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
    repositories {
        jcenter()
        mavenCentral()
    }

    apply(plugin = "io.github.art.project")
    apply(plugin = "com.jfrog.bintray")
    apply(plugin = "com.jfrog.artifactory")
    apply(plugin = "maven-publish")

    art {
        idea()
        lombok()
        tests()
    }

    if (bintrayUser.isNullOrEmpty() || bintrayKey.isNullOrEmpty()) {
        return@subprojects
    }

    afterEvaluate {
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
                websiteUrl = "https://github.com/art-community/art"
                vcsUrl = "https://github.com/art-community/art"
                setLabels("art", "kotlin", "java", "rsocket", "tarantool", "grpc", "protobuf", "rocksdb", "http", "tomcat")
                setLicenses("Apache-2.0")
            })
            tasks["bintrayUpload"].dependsOn(tasks["generatePomFileFor${name.capitalize()}Publication"], jar, sourceJar)
        }
    }
}

afterEvaluate {
    tasks["bintrayUpload"].enabled = false
    tasks["bintrayPublish"].enabled = false
    tasks["artifactoryPublish"].enabled = false
}