plugins {
    id("adkProject") version "1.0"
}

group = "ru.art"

adk {
    providedModules {
        applicationCore()
        applicationEntity()
        applicationService()
        applicationLogging()
        applicationConfig()
        applicationConfigRemote()
        applicationProtobuf()
        applicationGrpcClient()
        applicationGrpcServer()
        applicationHttp()
        applicationHttpJson()
        applicationHttpXml()
        applicationHttpClient()
        applicationHttpServer()
        applicationMetrics()
        applicationMetricsHttp()
        applicationSoapClient()
        applicationSoapServer()
        applicationNetworkManager()
        applicationKafkaConsumer()
        applicationKafkaProducer()
        applicationReactiveService()
        applicationRsocket()
        applicationSql()
        applicationRocksDb()
        applicationTarantool()
        applicationSoap()
    }
}