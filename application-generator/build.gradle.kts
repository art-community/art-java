plugins {
    id("adkProject") version "1.0"
}

group = "ru.art"

adk {
    providedModules {
        applicationCore()
        applicationEntity()
        applicationService()
        applicationHttp()
        applicationHttpServer()
        applicationHttpClient()
    }
}

dependencies {
    embedded("com.squareup", "javapoet", "1.11.+")
}
