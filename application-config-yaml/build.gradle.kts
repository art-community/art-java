adk {
    providedModules {
        applicationCore()
        applicationLogging()
        applicationEntity()
    }
}

dependencies {
    with(adk.externalDependencyVersionsConfiguration) {
        embedded("io.advantageous.konf", "konf", konfVersion)
        embedded("com.esotericsoftware.yamlbeans", "yamlbeans", yamlbeansVersion)
    }
}