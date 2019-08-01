plugins {
    id("adkProject") version "1.0"
}

group = "ru.adk"

adk {
    providedModules {
        applicationCore()
        applicationEntity()
        applicationLogging()
        applicationService()
    }
}

dependencies {
    with(adk.externalDependencyVersionsConfiguration) {
        embedded("io.micrometer", "micrometer-registry-prometheus", micrometerPrometheusVersion)
        embedded("io.github.mweirauch", "micrometer-jvm-extras", micrometerJvmExtrasVersion)
        embedded("io.prometheus", "simpleclient_dropwizard", prometheusDropwizardSimpleClient)
        embedded("io.dropwizard.metrics", "metrics-core", dropwizardVersions)
        embedded("io.dropwizard.metrics", "metrics-jvm", dropwizardVersions)
    }
}