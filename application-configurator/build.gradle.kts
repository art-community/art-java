plugins {
    id("adkProject") version "1.0"
    java
    `java-library`
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
        applicationProtobuf()
        applicationProtobufGenerated()
        applicationGrpc()
        applicationGrpcClient()
        applicationGrpcServer()
        applicationJson()
        applicationHttp()
        applicationHttpClient()
        applicationMetrics()
        applicationMetricsHttp()
        applicationHttpJson()
        applicationHttpServer()
        applicationRocksDb()
        applicationConfiguratorApi()
        applicationConfigRemoteApi()
    }
}