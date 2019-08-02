adk {
    withProtobufGenerator()
}

dependencies {
    with(adk.externalDependencyVersionsConfiguration) {
        provided("io.grpc", "grpc-stub", grpcVersion).exclude("com.google.guava", "guava")
        provided("io.grpc", "grpc-protobuf", grpcVersion).exclude("com.google.guava", "guava")
    }
}