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
        applicationConfig()
        applicationConfigRemoteApi()
        applicationConfiguratorApi()
        applicationGrpcClient()
        applicationGrpcServer()
    }
}