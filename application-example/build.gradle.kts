plugins {
    id("adkProject") version "1.0"
}

group = "ru.adk"

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
        applicationScheduler()
        applicationConfigExtensions()
        applicationProtobuf()
        applicationProtobufGenerated()
        applicationGrpc()
        applicationGrpcClient()
        applicationGrpcServer()
        applicationJson()
        applicationXml()
        applicationHttp()
        applicationSoap()
        applicationMetrics()
        applicationMetricsHttp()
        applicationHttpJson()
        applicationHttpXml()
        applicationHttpClient()
        applicationHttpServer()
        applicationSoapClient()
        applicationSoapServer()
        applicationSql()
        applicationRocksDb()
        applicationExampleApi()
        applicationRsocket()
        applicationReactiveService()
    }
}

dependencies {
    embedded("com.oracle", "ojdbc6", "11.+")
}