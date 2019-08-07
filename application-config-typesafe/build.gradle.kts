art {
    providedModules {
        applicationCore()
        applicationEntity()
    }
}

dependencies {
    with(art.externalDependencyVersionsConfiguration) {
        embedded("io.advantageous.konf", "konf-typesafe-config", konfTypesafeConfigVersion)
        embedded("io.advantageous.konf", "konf", konfVersion)
        embedded("com.typesafe", "config", typesafeConfigVersion)
    }
}