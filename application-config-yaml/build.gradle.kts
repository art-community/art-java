plugins {
    id("adkProject") version "1.0"
}

group = "ru.art"

adk {
    providedModules {
        applicationCore()
        applicationLogging()
        applicationEntity()
    }
}

dependencies {
    with(adk.externalDependencyVersionsConfiguration) {
        embedded("io.advantageous.konf", "konf", konfVersion)
        embedded("com.esotericsoftware.yamlbeans", "yamlbeans", yamlbeansVersion)
    }
}