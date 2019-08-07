art {
    providedModules {
        applicationCore()
        applicationEntity()
        applicationLogging()
        applicationService()
        applicationReactiveService()
        applicationProtobuf()
        applicationProtobufGenerated()
        applicationJson()
        applicationXml()
    }
}

dependencies {
    embedded("io.rsocket", "rsocket-core", art.externalDependencyVersionsConfiguration.rsocketVersion)
    embedded("io.rsocket", "rsocket-transport-netty", art.externalDependencyVersionsConfiguration.rsocketVersion)
}