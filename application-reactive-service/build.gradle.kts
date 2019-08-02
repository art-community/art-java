adk {
    providedModules {
        applicationCore()
        applicationEntity()
        applicationService()
        applicationLogging()
    }
}

dependencies {
    embedded("io.projectreactor", "reactor-core", adk.externalDependencyVersionsConfiguration.projectReactorVersion)
}