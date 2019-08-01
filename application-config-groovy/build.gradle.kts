plugins {
    id("adkProject") version "1.0"
}

group = "ru.adk"

adk {
    providedModules {
        applicationCore()
        applicationEntity()
    }
}

dependencies {
    embedded("org.codehaus.groovy", "groovy-all", adk.externalDependencyVersionsConfiguration.groovyVersion)
}