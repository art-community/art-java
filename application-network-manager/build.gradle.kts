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
        applicationProtobuf()
        applicationGrpcClient()
        applicationGrpcServer()
        applicationHttpServer()
        applicationStateApi()
        applicationScheduler()
    }
}