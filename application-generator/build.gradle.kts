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
    embedded("com.squareup", "javapoet", art.externalDependencyVersionsConfiguration.javaPoetVersion)
}
