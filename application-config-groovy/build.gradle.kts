adk {
    providedModules {
        applicationCore()
        applicationEntity()
    }
}

dependencies {
    embedded("org.codehaus.groovy", "groovy-all", adk.externalDependencyVersionsConfiguration.groovyVersion)
}