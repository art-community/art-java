plugins {
    id("adkProject") version "1.0"
}

group = "ru.adk"

adk {
    providedModules {
        applicationCore()
        applicationEntity()
        applicationConfigTypesafe()
        applicationConfigYaml()
        applicationConfigGroovy()
    }
}