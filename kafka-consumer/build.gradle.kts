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

val kafkaVersion: String by project

dependencies {
    implementation(project(":core"))
    implementation(project(":entity"))
    implementation(project(":logging"))
    implementation(project(":service"))
    implementation(project(":kafka-client"))

    api("org.apache.kafka", "kafka-streams", kafkaVersion)
            .exclude("org.apache.kafka", "kafka-clients")
            .exclude("com.fasterxml.jackson.core")
            .exclude("org.slf4j")
}
