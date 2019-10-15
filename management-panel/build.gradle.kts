import org.gradle.api.JavaVersion.*
import org.jetbrains.kotlin.gradle.tasks.*
import org.gradle.internal.os.OperatingSystem as os

art {
    embeddedModules {
        kit()
    }
    web {
        buildToolCheckingCommand = if (os.current().isWindows) arrayOf("cmd", "/c", "yarn", "--no-lockfile", "--skip-integrity-check") else arrayOf("yarn", "--no-lockfile", "--skip-integrity-check")
        buildWebCommand = if (os.current().isWindows) listOf("cmd", "/c", "yarn", "run", "production") else listOf("yarn", "run", "production")
        prepareWebCommand = if (os.current().isWindows) listOf("cmd", "/c", "yarn") else listOf("yarn")
    }
    mainClass("ru.art.platform.module.ManagementPanelModule")
}

dependencies {
    embedded(project(":api"))
    embedded("com.auth0:java-jwt:3.8.+") {
        exclude("com.fasterxml.jackson.dataformat")
        exclude("com.fasterxml.jackson.core")
    }
    embedded("com.github.docker-java", "docker-java", "3.1.5")
}


tasks.withType<KotlinCompile> {
    sourceCompatibility = VERSION_1_8.toString()
    targetCompatibility = VERSION_1_8.toString()

    kotlinOptions {
        jvmTarget = VERSION_1_8.toString()
    }
}

tasks.withType(JavaCompile::class).forEach { task -> task.dependsOn(":api:build") }