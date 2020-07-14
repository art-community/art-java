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

val micrometerPrometheusVersion by project
val micrometerJvmExtrasVersion by project
val prometheusDropwizardSimpleClientVersion by project
val dropwizardMetricsVersion by project

dependencies {
    implementation(project(":core"))
    implementation(project(":entity"))
    implementation(project(":logging"))
    implementation(project(":service"))

    api("io.micrometer", "micrometer-registry-prometheus", micrometerPrometheusVersion)

    api("io.github.mweirauch", "micrometer-jvm-extras", micrometerJvmExtrasVersion)
            .exclude("org.slf4j")

    api("io.prometheus", "simpleclient_dropwizard", prometheusDropwizardSimpleClientVersion)
            .exclude("org.slf4j")

    api("io.dropwizard.metrics", "metrics-jvm", dropwizardMetricsVersion)
            .exclude("io.dropwizard.metrics", "metrics-core")
            .exclude("org.slf4j")
}
