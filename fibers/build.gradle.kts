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

plugins {
    id("art-internal-jvm")
}

dependencies {
    val graalVersion: String by project

    embedded(project(":core"))
    api(project(":meta"))
    implementation(project(":logging"))

    compileOnly("org.graalvm.nativeimage", "svm", graalVersion)
}

executable {
    main("io.art.fibers.Fibers")
    native {
        addGraalOptions("-H:GenerateDebugInfo=1")
        addGraalOptions("-Xss=1048576")
    }
}
