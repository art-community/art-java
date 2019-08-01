import ru.adk.gradle.plugin.configuration.*

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
    }
    testModules {
        applicationConfig()
        applicationConfigYaml()
        applicationConfigRemote()
        applicationConfigExtensions(::substituteWithArtifact)
        applicationHttp()
        applicationMetrics()
        applicationMetricsHttp()
        applicationJson()
        applicationHttpJson()
        applicationHttpServer()
    }
    resources {
        resourceDirs.add("src/main/templates")
        resourceDirs.add("src/main/tarantool")
        resourceDirs.add("src/main/lua")
    }
    withSpockFramework()
}

dependencies {
    embedded("org.jtwig", "jtwig-web", adk.externalDependencyVersionsConfiguration.jtwigVersion)
            .exclude("com.google.guava", "guava")
            .exclude("org.apache.httpcomponents", "httpclient")
    embedded("org.zeroturnaround", "zt-exec", "1.10")
    embedded("org.zeroturnaround", "zt-exec", "1.10")
    embedded("org.apache.logging.log4j", "log4j-iostreams", "2.11.2")
    embedded("org.tarantool", "connector", "1.9.1")
    embedded("com.google.guava", "guava", adk.externalDependencyVersionsConfiguration.guavaVersion)
    embedded("org.apache.httpcomponents", "httpclient", adk.externalDependencyVersionsConfiguration.apacheHttpClientVersion)

}