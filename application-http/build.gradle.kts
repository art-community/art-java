plugins {
    id("adkProject") version "1.0"
}

group = "ru.adk"

adk {
    providedModules {
        applicationCore()
        applicationEntity()
        applicationLogging()
    }
}

dependencies {
    with(adk.externalDependencyVersionsConfiguration) {
        embedded("org.zalando", "logbook-core", logbookVersion)
        embedded("org.apache.httpcomponents", "httpcore", apacheHttpCoreVersion)
    }
}