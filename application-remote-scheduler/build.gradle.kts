plugins {
    id("adkProject") version "1.0"
}

group = "ru.art"

adk {
    embeddedModules {
        applicationCore()
        applicationEntity()
        applicationLogging()
        applicationService()
        applicationConfig()
        applicationConfigYaml()
        applicationConfigRemote()
        applicationConfigRemoteApi()
        applicationConfiguratorApi()
        applicationConfigExtensions()
        applicationHttp()
        applicationHttpJson()
        applicationHttpServer()
        applicationProtobuf()
        applicationProtobufGenerated()
        applicationGrpc()
        applicationGrpcClient()
        applicationGrpcServer()
        applicationSchedulerDbAdapterApi()
        applicationMetrics()
        applicationMetricsHttp()
        applicationScheduler()
        applicationRemoteSchedulerApi()
    }
}