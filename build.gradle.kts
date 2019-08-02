plugins {
    id("adkProject") version "1.0"
}

subprojects {
    apply(plugin = "adkProject")

    afterEvaluate {
        version = rootProject.version
    }
}