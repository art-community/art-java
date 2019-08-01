plugins {
    id("adkProject") version "1.0"
}

group = "ru.art"

adk {
    withSpockFramework()
    providedModules {
        applicationCore()
        applicationLogging()
    }
}