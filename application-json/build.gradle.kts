adk {
    providedModules {
        applicationCore()
        applicationEntity()
        applicationLogging()
    }
}

dependencies {
    with(adk.externalDependencyVersionsConfiguration) {
        embedded("com.fasterxml.jackson.core", "jackson-core", jacksonVersion)
        embedded("com.fasterxml.jackson.core", "jackson-databind", jacksonVersion)
    }
}