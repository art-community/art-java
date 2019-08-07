art {
    providedModules {
        applicationCore()
        applicationEntity()
        applicationLogging()
    }
}

dependencies {
    with(art.externalDependencyVersionsConfiguration) {
        embedded("com.fasterxml.jackson.core", "jackson-core", jacksonVersion)
        embedded("com.fasterxml.jackson.core", "jackson-databind", jacksonVersion)
    }
}