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
    id("groovy")
}

dependencies {
    implementation(project(":configurator"))
    implementation(project(":model"))
    implementation(project(":core"))
    implementation(project(":logging"))
    implementation(project(":value"))
    implementation(project(":json"))
    implementation(project(":protobuf"))
    implementation(project(":message-pack"))
    implementation(project(":xml"))
    implementation(project(":server"))
    implementation(project(":communicator"))
    implementation(project(":rsocket"))
    implementation(project(":tarantool"))

    testImplementation("org.codehaus.groovy:groovy:2.5.13")
    testImplementation(platform("org.spockframework:spock-bom:2.0-M3-groovy-2.5"))
    testImplementation("org.spockframework:spock-core")
    testImplementation("org.hamcrest:hamcrest-core:2.2")
    testImplementation("net.bytebuddy:byte-buddy:1.10.10")
    testImplementation("org.objenesis:objenesis:3.1")
}
