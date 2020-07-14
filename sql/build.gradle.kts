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

art {
    providedModules {
        applicationCore()
        applicationLogging()
        applicationMetrics()
    }
}

dependencies {
    with(art.externalDependencyVersionsConfiguration) {
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
        embedded("org.jooq", "jooq", jooqVersion)
                .exclude("org.slf4j")
                .exclude("com.google.guava")
                .exclude("com.google.code.findbugs")
        embedded("com.zaxxer", "HikariCP", hikariVersion)
                .exclude("com.google.guava")
                .exclude("com.google.code.findbugs")
                .exclude("org.slf4j")
        embedded("org.apache.tomcat", "tomcat-jdbc", tomcatVersion)
        dropwizardExclusions(embedded("io.dropwizard", "dropwizard-util", dropwizardVersion))
        dropwizardExclusions(embedded("io.dropwizard", "dropwizard-validation", dropwizardVersion))
        dropwizardExclusions(embedded("io.dropwizard", "dropwizard-lifecycle", dropwizardVersion))
        dropwizardExclusions(embedded("io.dropwizard", "dropwizard-db", dropwizardVersion))
    }
}
