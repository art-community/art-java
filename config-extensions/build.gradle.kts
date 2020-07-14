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
    implementation(project(":core"))
    implementation(project(":entity"))
    implementation(project(":service"))
    implementation(project(":logging"))
    implementation(project(":config"))
    implementation(project(":config-remote"))
    implementation(project(":protobuf"))
    implementation(project(":grpc-client"))
    implementation(project(":grpc-server"))
    implementation(project(":http"))
    implementation(project(":http-json"))
    implementation(project(":http-xml"))
    implementation(project(":http-client"))
    implementation(project(":http-server"))
    implementation(project(":metrics"))
    implementation(project(":metrics-http"))
    implementation(project(":soap-client"))
    implementation(project(":soap-server"))
    implementation(project(":network-manager"))
    implementation(project(":kafka-client"))
    implementation(project(":kafka-consumer"))
    implementation(project(":kafka-producer"))
    implementation(project(":reactive-service"))
    implementation(project(":rsocket"))
    implementation(project(":sql"))
    implementation(project(":rocks-db"))
    implementation(project(":tarantool"))
    implementation(project(":soap"))
}
