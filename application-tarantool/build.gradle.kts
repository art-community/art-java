art {
    providedModules {
        applicationCore()
        applicationEntity()
        applicationLogging()
        applicationService()
    }
    resources {
        resourceDirs.add("src/main/templates")
        resourceDirs.add("src/main/tarantool")
        resourceDirs.add("src/main/lua")
    }
}

dependencies {
    with(art.externalDependencyVersionsConfiguration) {
        embedded("org.jtwig", "jtwig-web", jtwigVersion)
                .exclude("com.google.guava", "guava")
                .exclude("org.apache.httpcomponents", "httpclient")
        embedded("org.zeroturnaround", "zt-exec", zeroTurnaroundVersion)
        embedded("org.apache.logging.log4j", "log4j-iostreams", log4jVersion)
        embedded("org.tarantool", "connector", tarantoolConnectorVersion)
        embedded("com.google.guava", "guava", guavaVersion)
        embedded("org.apache.httpcomponents", "httpclient", apacheHttpClientVersion)
    }
}
configurations {
        with(embedded.get()) {
            exclude("org.slf4j", "slf4j-api")
            exclude("org.slf4j", "slf4j-log4j12")
            exclude("org.slf4j", "jul-to-slf4j")
        }
    }