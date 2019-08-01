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
        applicationConfiguratorApi()
        applicationConfigRemoteApi()
        applicationConfigExtensions()
        applicationHttp()
        applicationHttpClient()
        applicationJson()
        applicationHttpJson()
        applicationHttpServer()
        applicationProtobuf()
        applicationProtobufGenerated()
        applicationGrpc()
        applicationGrpcServer()
        applicationGrpcClient()
        applicationMetrics()
        applicationMetricsHttp()
    }
}