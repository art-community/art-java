art {
    providedModules {
        applicationCore()
        applicationEntity()
        applicationLogging()
        applicationService()
        applicationKafka()
    }
}

dependencies {
    with(art.externalDependencyVersionsConfiguration) {
        embedded("org.apache.kafka", "kafka-streams", kafkaVersion)
    }
}