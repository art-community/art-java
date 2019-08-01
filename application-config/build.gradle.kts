plugins {
    id("adkProject") version "1.0"
}

group = "ru.art"

adk {
    providedModules {
        applicationCore()
        applicationEntity()
        applicationConfigTypesafe()
        applicationConfigYaml()
        applicationConfigGroovy()
    }
}