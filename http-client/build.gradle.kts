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
    val apacheHttpClientVersion: String by project
    val apacheHttpAsyncClientVersion: String by project
    val logbookVersion: String by project

    implementation(project(":core"))
    implementation(project(":entity"))
    implementation(project(":logging"))
    implementation(project(":server"))
    implementation(project(":http"))

    api("org.apache.httpcomponents", "httpclient", apacheHttpClientVersion)
            .exclude("org.apache.httpcomponents", "httpcore")
            .exclude("commons-logging")

    api("org.apache.httpcomponents", "httpasyncclient", apacheHttpAsyncClientVersion)
            .exclude("org.apache.httpcomponents", "httpcore")
            .exclude("commons-logging")

    api("org.zalando", "logbook-httpclient", logbookVersion)
            .exclude("org.zalando", "logbook-core")
            .exclude("org.zalando", "logbook-api")
            .exclude("org.zalando", "faux-pas")
            .exclude("org.apiguardian")
            .exclude("org.apache.httpcomponents", "httpcore")
            .exclude("org.slf4j")
            .exclude("commons-logging")
}
