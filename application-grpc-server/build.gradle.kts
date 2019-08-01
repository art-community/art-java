plugins {
    id("adkProject") version "1.0"
}

group = "ru.art"

adk {
    providedModules {
        applicationCore()
        applicationEntity()
        applicationLogging()
        applicationService()
        applicationProtobuf()
        applicationProtobufGenerated()
        applicationGrpc()
    }
}