import ru.art.gradle.configuration.*

art {
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
        applicationConfigExtensions(project::substituteWithArtifact)
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
    embedded("org.jtwig", "jtwig-web", art.externalDependencyVersionsConfiguration.jtwigVersion)
            .exclude("com.google.guava", "guava")
            .exclude("org.apache.httpcomponents", "httpclient")
    embedded("org.zeroturnaround", "zt-exec", "1.10")
    embedded("org.zeroturnaround", "zt-exec", "1.10")
    embedded("org.apache.logging.log4j", "log4j-iostreams", "2.11.2")
    embedded("org.tarantool", "connector", "1.9.1")
    embedded("com.google.guava", "guava", art.externalDependencyVersionsConfiguration.guavaVersion)
    embedded("org.apache.httpcomponents", "httpclient", art.externalDependencyVersionsConfiguration.apacheHttpClientVersion)

}