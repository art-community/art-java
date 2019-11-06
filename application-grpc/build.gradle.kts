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

art {
    providedModules {
        applicationCore()
        applicationProtobuf()
    }
}

dependencies {
    with(art.externalDependencyVersionsConfiguration) {
        embedded("io.grpc", "grpc-stub", grpcVersion)
                .exclude("io.grpc", "grpc-api")
        embedded("io.grpc", "grpc-netty", grpcVersion)
                .exclude("io.grpc", "grpc-api")
                .exclude("io.netty")
        embedded("io.grpc", "grpc-netty-shaded", grpcVersion)
                .exclude("io.grpc", "grpc-api")
                .exclude("io.netty")
        embedded("io.netty", "netty-all", "4.1.42.Final")
    }
}

configurations {
    with(embedded.get()) {
        exclude("com.google.guava")
        exclude("com.google.code.findbugs")
    }
}