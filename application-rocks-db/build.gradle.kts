art {
    providedModules {
        applicationCore()
        applicationEntity()
        applicationLogging()
        applicationProtobuf()
        applicationProtobufGenerated()
    }
}

dependencies {
    embedded("org.rocksdb", "rocksdbjni", art.externalDependencyVersionsConfiguration.rocksdbVersion)
}