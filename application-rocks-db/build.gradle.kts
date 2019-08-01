plugins {
    id("adkProject") version "1.0"
}

group = "ru.art"

adk {
    providedModules {
        applicationCore()
        applicationEntity()
        applicationLogging()
        applicationProtobuf()
        applicationProtobufGenerated()
    }
}

dependencies {
    embedded("org.rocksdb", "rocksdbjni", adk.externalDependencyVersionsConfiguration.rocksdbVersion)
}