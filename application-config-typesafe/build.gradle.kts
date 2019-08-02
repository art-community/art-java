adk {
    providedModules {
        applicationCore()
        applicationEntity()
    }
}

dependencies {
    with(adk.externalDependencyVersionsConfiguration) {
        embedded("io.advantageous.konf", "konf-typesafe-config", konfTypesafeConfigVersion)
        embedded("io.advantageous.konf", "konf", konfVersion)
        embedded("com.typesafe", "config", typesafeConfigVersion)
    }
}