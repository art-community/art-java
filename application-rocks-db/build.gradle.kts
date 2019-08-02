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