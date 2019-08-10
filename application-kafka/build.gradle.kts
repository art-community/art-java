art {
    providedModules {
        applicationCore()
        applicationEntity()
        applicationLogging()
        applicationJson()
        applicationProtobuf()
        applicationProtobufGenerated()
    }
}

configurations {
    with(embedded.get()) {
        exclude("org.slf4j", "slf4j-api")
        exclude("org.slf4j", "slf4j-log4j12")
        exclude("org.slf4j", "jul-to-slf4j")
    }
}

dependencies {
    with(art.externalDependencyVersionsConfiguration) {
        embedded("org.apache.kafka", "kafka-clients", kafkaVersion)
        embedded("org.apache.kafka", "kafka-log4j-appender", kafkaLog4jAppenderVersion)
    }
}