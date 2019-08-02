



adk {
    providedModules {
        applicationCore()
        applicationEntity()
        applicationLogging()
    }
}

dependencies {
    embedded("org.jvnet.staxex", "stax-ex", adk.externalDependencyVersionsConfiguration.staxVersion)
}