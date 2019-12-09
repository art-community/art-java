import org.eclipse.jgit.api.Git.*

/*
 * ART Java
 *
 * Copyright 2019 ART
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
    id("org.ajoberstar.grgit") version "1.7.2"
}

task("getReindexer") {
    group = "reindexer"
    doLast {
        if (!file("$projectDir/src/main/cpp/reindexer").exists()) {
            cloneRepository()
                    .setURI("https://github.com/Restream/reindexer")
                    .setDirectory(file("$projectDir/src/main/cpp/reindexer"))
                    .call()
            return@doLast
        }
        open(file("$projectDir/src/main/cpp/reindexer"))
                .pull()
                .setRemote("origin")
                .setRemoteBranchName("master")
                .call()
    }
}