



art {
    providedModules {
        applicationCore()
        applicationEntity()
        applicationLogging()
    }
}

dependencies {
    embedded("org.jvnet.staxex", "stax-ex", art.externalDependencyVersionsConfiguration.staxVersion)
}