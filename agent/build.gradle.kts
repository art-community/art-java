import org.gradle.api.JavaVersion.*
import org.jetbrains.kotlin.gradle.tasks.*

art {
    embeddedModules {
        kit()
    }
    mainClass("ru.art.platform.agent.module.AgentModule")
}


dependencies {
    embedded(project(":api"))
}


tasks.withType<KotlinCompile> {
    sourceCompatibility = VERSION_1_8.toString()
    targetCompatibility = VERSION_1_8.toString()

    kotlinOptions {
        jvmTarget = VERSION_1_8.toString()
    }
}

tasks.withType(JavaCompile::class).forEach { task -> task.dependsOn(":api:build") }