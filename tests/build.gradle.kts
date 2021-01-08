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
    val groovyVersion: String by project
    val spockBomVersion: String by project
    val hamcrestCoreVersion: String by project
    val byteBuddyVersion: String by project
    val objenesisVersion: String by project


    implementation(project(":launcher"))
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


    testImplementation("org.codehaus.groovy:groovy:$groovyVersion")
    testImplementation(platform("org.spockframework:spock-bom:$spockBomVersion"))
    testImplementation("org.spockframework:spock-core")
    testImplementation("org.hamcrest:hamcrest-core:$hamcrestCoreVersion")
    testImplementation("net.bytebuddy:byte-buddy:$byteBuddyVersion")
    testImplementation("org.objenesis:objenesis:$objenesisVersion")
}
