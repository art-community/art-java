adk {
    providedModules {
        applicationCore()
        applicationEntity()
        applicationLogging()
    }
}

dependencies {
    with(adk.externalDependencyVersionsConfiguration) {
        embedded("org.zalando", "logbook-core", logbookVersion)
        embedded("org.apache.httpcomponents", "httpcore", apacheHttpCoreVersion)
    }
}