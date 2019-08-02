adk {
    providedModules {
        applicationCore()
        applicationLogging()
        applicationMetrics()
    }
}

dependencies {
    with(adk.externalDependencyVersionsConfiguration) {
        embedded("org.jooq", "jooq", jooqVersion)
        embedded("com.zaxxer", "HikariCP", hikariVersion)
        embedded("org.apache.tomcat", "tomcat-jdbc", tomcatVersion)
    }
}