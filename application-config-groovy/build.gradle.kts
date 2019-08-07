art {
    providedModules {
        applicationCore()
        applicationEntity()
    }
}

dependencies {
    embedded("org.codehaus.groovy", "groovy-all", art.externalDependencyVersionsConfiguration.groovyVersion)
}