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

dependencies {
    val jooqVersion: String by project
    val hikariVersion: String by project
    val tomcatVersion: String by project
    val dropwizardVersion: String by project

    implementation(project(":core"))
    implementation(project(":logging"))
    implementation(project(":metrics"))

    fun dropwizardExclusions(dependency: ExternalModuleDependency) {
        with(dependency) {
            exclude("io.dropwizard")
            exclude("com.google.guava")
            exclude("com.google.code.findbugs")
            exclude("org.slf4j")
            exclude("com.google.guava")
            exclude("com.google.code.findbugs")
            exclude("org.eclipse.jetty")
            exclude("org.slf4j")
        }
    }

    api("org.jooq", "jooq", jooqVersion)
            .exclude("org.slf4j")
            .exclude("com.google.guava")
            .exclude("com.google.code.findbugs")

    api("com.zaxxer", "HikariCP", hikariVersion)
            .exclude("com.google.guava")
            .exclude("com.google.code.findbugs")
            .exclude("org.slf4j")

    api("org.apache.tomcat", "tomcat-jdbc", tomcatVersion)

    dropwizardExclusions(api("io.dropwizard", "dropwizard-util", dropwizardVersion))
    dropwizardExclusions(api("io.dropwizard", "dropwizard-validation", dropwizardVersion))
    dropwizardExclusions(api("io.dropwizard", "dropwizard-lifecycle", dropwizardVersion))
    dropwizardExclusions(api("io.dropwizard", "dropwizard-db", dropwizardVersion))
}
