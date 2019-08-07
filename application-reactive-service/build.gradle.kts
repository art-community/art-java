art {
    providedModules {
        applicationCore()
        applicationEntity()
        applicationService()
        applicationLogging()
    }
}

dependencies {
    embedded("io.projectreactor", "reactor-core", art.externalDependencyVersionsConfiguration.projectReactorVersion)
}