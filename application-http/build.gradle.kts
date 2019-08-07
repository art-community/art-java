art {
    providedModules {
        applicationCore()
        applicationEntity()
        applicationLogging()
    }
}

dependencies {
    with(art.externalDependencyVersionsConfiguration) {
        embedded("org.zalando", "logbook-core", logbookVersion)
        embedded("org.apache.httpcomponents", "httpcore", apacheHttpCoreVersion)
    }
}