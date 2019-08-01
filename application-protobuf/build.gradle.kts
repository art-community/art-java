plugins {
    id("adkProject") version "1.0"
}

group = "ru.art"

adk {
    providedModules {
        applicationCore()
        applicationEntity()
        applicationService()
        applicationProtobufGenerated()
    }
}

dependencies {
    with(adk.externalDependencyVersionsConfiguration) {
        embedded("io.grpc", "grpc-protobuf", grpcVersion).exclude("com.google.guava", "guava")
        embedded("com.google.guava", "guava", guavaVersion)
    }
}