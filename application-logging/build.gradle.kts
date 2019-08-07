art {
    providedModules {
        applicationCore()
    }
}

dependencies {
    with(art.externalDependencyVersionsConfiguration) {
        embedded("org.slf4j", "jul-to-slf4j", sl4jVersion)

        embedded("org.apache.logging.log4j", "log4j-api", log4jVersion)
        embedded("org.apache.logging.log4j", "log4j-core", log4jVersion)
        embedded("org.apache.logging.log4j", "log4j-slf4j-impl", log4jVersion)

        embedded("com.fasterxml.jackson.dataformat", "jackson-dataformat-yaml", jacksonVersion)
        embedded("com.fasterxml.jackson.core", "jackson-core", jacksonVersion)
        embedded("com.fasterxml.jackson.core", "jackson-databind", jacksonVersion)
    }
}