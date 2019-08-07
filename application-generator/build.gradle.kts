art {
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
