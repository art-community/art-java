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

plugins {
    id("com.gradle.enterprise") version "3.3.4"
}
rootProject.name = "art-java"
include("core")
include("model")
include("value")
include("logging")
include("resilience")
include("launcher")
include("configurator")
include("yaml-configuration")
include("json")
include("protobuf")
include("xml")
include("message-pack")
include("template-engine")
include("metrics")
include("server")
include("communicator")
include("metrics-http")
include("scheduler")
include("grpc")
include("grpc-client")
include("grpc-server")
include("http")
include("http-client")
include("http-json")
include("http-server")
include("http-xml")
include("kafka-client")
include("kafka-consumer")
include("kafka-producer")
include("module-executor")
include("rsocket")
include("soap")
include("soap-client")
include("soap-server")
include("rocks-db")
include("sql")
include("tarantool")
include("example")
