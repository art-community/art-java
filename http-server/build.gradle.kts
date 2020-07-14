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
    val tomcatVersion: String by project
    val logbookVersion: String by project
    val log4jVersion: String by project

    implementation(project(":core"))
    implementation(project(":entity"))
    implementation(project(":logging"))
    implementation(project(":service"))
    implementation(project(":http"))
    implementation(project(":metrics"))
    implementation(project(":template-engine"))

    api("org.apache.tomcat.embed", "tomcat-embed-core", tomcatVersion)
            .exclude("org.apache.httpcomponents", "httpcore")

    api("org.apache.tomcat", "tomcat-servlet-api", tomcatVersion)

    api("org.zalando", "logbook-servlet", logbookVersion)
            .exclude("org.zalando", "logbook-core")
            .exclude("org.zalando", "logbook-api")
            .exclude("org.zalando", "faux-pas")
            .exclude("org.apiguardian")
            .exclude("org.slf4j")
            .exclude("commons-logging")

    api("org.apache.logging.log4j", "log4j-web", log4jVersion)
            .exclude("org.apache.logging.log4j")
}
