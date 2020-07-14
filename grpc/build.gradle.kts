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
    val nettyVersion: String  by project
    val grpcVersion: String by project

    implementation(project(":core"))
    implementation(project(":protobuf"))

    api("io.grpc", "grpc-stub", grpcVersion)
            .exclude("io.grpc", "grpc-api")
            .exclude("com.google.guava")
            .exclude("com.google.code.findbugs")

    api("io.grpc", "grpc-netty-shaded", grpcVersion)
            .exclude("io.grpc", "grpc-api")
            .exclude("io.netty")
            .exclude("com.google.guava")
            .exclude("com.google.code.findbugs")

    api("io.netty", "netty-all", nettyVersion)
            .exclude("com.google.guava")
            .exclude("com.google.code.findbugs")
}
