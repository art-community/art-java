plugins {
    id("adkProject") version "1.0"
}

group = "ru.adk"

adk {
    providedModules {
        applicationCore()
        applicationEntity()
        applicationLogging()
        applicationService()
        applicationXml()
        applicationSoap()
        applicationHttp()
        applicationHttpServer()
    }
}