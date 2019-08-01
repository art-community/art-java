plugins {
    id("adkProject") version "1.0"
}

group = "ru.art"

adk {
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
    embedded("io.rsocket", "rsocket-core", adk.externalDependencyVersionsConfiguration.rsocketVersion)
    embedded("io.rsocket", "rsocket-transport-netty", adk.externalDependencyVersionsConfiguration.rsocketVersion)
}