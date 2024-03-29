/*
 * ART
 *
 * Copyright 2019-2022 ART
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
    api(project(":core"))
    api(project(":meta"))

    val jacksonVersion: String by project
    api("com.fasterxml.jackson.dataformat", "jackson-dataformat-yaml", jacksonVersion)
    api("com.fasterxml.jackson.core", "jackson-core", jacksonVersion)
    api("com.fasterxml.jackson.core", "jackson-databind", jacksonVersion)

    testImplementation(testFixtures(project(":meta")))
}
