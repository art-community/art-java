plugins {
    id("adkProject") version "1.0"
}

group = "ru.adk"

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