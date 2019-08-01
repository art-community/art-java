plugins {
    id("adkProject") version "1.0"
}

group = "ru.adk"

adk {
    providedModules {
        applicationCore()
        applicationProtobuf()
        applicationProtobufGenerated()
    }
}

dependencies {
    with(adk.externalDependencyVersionsConfiguration) {
        embedded("io.grpc", "grpc-stub", grpcVersion).exclude("com.google.guava", "guava")
        embedded("io.grpc", "grpc-netty", grpcVersion).exclude("com.google.guava", "guava")
        embedded("io.grpc", "grpc-netty-shaded", grpcVersion).exclude("com.google.guava", "guava")
    }
}