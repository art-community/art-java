plugins {
    id("adkProject") version "1.0"
}

group = "ru.art"

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