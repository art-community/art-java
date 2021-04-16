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

dependencies {
    val resilience4jVersion: String by project

    implementation(project(":core"))
    api("io.github.resilience4j", "resilience4j-circuitbreaker", resilience4jVersion)
            .exclude("com.google.guava")
            .exclude("io.vavr")
            .exclude("org.slf4j")
    api("io.github.resilience4j", "resilience4j-ratelimiter", resilience4jVersion)
            .exclude("com.google.guava")
            .exclude("io.vavr")
            .exclude("org.slf4j")
    api("io.github.resilience4j", "resilience4j-retry", resilience4jVersion)
            .exclude("com.google.guava")
            .exclude("io.vavr")
            .exclude("org.slf4j")
    api("io.github.resilience4j", "resilience4j-metrics", resilience4jVersion)
            .exclude("com.google.guava")
            .exclude("io.vavr")
            .exclude("org.slf4j")
    api("io.github.resilience4j", "resilience4j-bulkhead", resilience4jVersion)
            .exclude("com.google.guava")
            .exclude("io.vavr")
            .exclude("org.slf4j")
    api("io.github.resilience4j", "resilience4j-timelimiter", resilience4jVersion)
            .exclude("com.google.guava")
            .exclude("io.vavr")
            .exclude("org.slf4j")
    api("io.github.resilience4j", "resilience4j-reactor", resilience4jVersion)
            .exclude("com.google.guava")
            .exclude("io.vavr")
            .exclude("org.slf4j")
}
