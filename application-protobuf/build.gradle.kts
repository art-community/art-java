art {
    providedModules {
        applicationCore()
        applicationEntity()
        applicationService()
        applicationProtobufGenerated()
    }
}

dependencies {
    with(art.externalDependencyVersionsConfiguration) {
        embedded("io.grpc", "grpc-protobuf", grpcVersion).exclude("com.google.guava", "guava")
        embedded("com.google.guava", "guava", guavaVersion)
    }
}