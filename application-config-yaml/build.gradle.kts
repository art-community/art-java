art {
    providedModules {
        applicationCore()
        applicationLogging()
        applicationEntity()
    }
}

dependencies {
    with(art.externalDependencyVersionsConfiguration) {
        embedded("io.advantageous.konf", "konf", konfVersion)
        embedded("com.esotericsoftware.yamlbeans", "yamlbeans", yamlbeansVersion)
    }
}