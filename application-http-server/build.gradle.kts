



adk {
    providedModules {
        applicationCore()
        applicationEntity()
        applicationLogging()
        applicationService()
        applicationHttp()
        applicationMetrics()
    }
}

dependencies {
    with(adk.externalDependencyVersionsConfiguration) {
        embedded("org.apache.tomcat.embed", "tomcat-embed-core", tomcatVersion)
                .exclude("org.apache.tomcat", "tomcat-juli")
                .exclude("org.apache.httpcomponents", "httpcore")
        embedded("org.apache.tomcat", "tomcat-servlet-api", tomcatVersion)
        embedded("org.zalando", "logbook-servlet", logbookVersion)
        embedded("org.jtwig", "jtwig-web", jtwigVersion)
                .exclude("com.google.guava", "guava")
                .exclude("org.apache.httpcomponents", "httpclient")
        embedded("com.google.guava", "guava", guavaVersion)
        embedded("org.apache.httpcomponents", "httpclient", apacheHttpClientVersion)
                .exclude("org.apache.httpcomponents", "httpcore")
    }
}