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
        applicationHttp()
    }
}

dependencies {
    with(adk.externalDependencyVersionsConfiguration) {
        embedded("org.apache.httpcomponents", "httpclient", apacheHttpClientVersion)
                .exclude("org.apache.httpcomponents", "httpcore")
        embedded("org.apache.httpcomponents", "httpasyncclient", apacheHttpAsyncClientVersion)
                .exclude("org.apache.httpcomponents", "httpcore")
        embedded("org.zalando", "logbook-httpclient", logbookVersion)
    }
}