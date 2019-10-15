

import org.gradle.api.JavaVersion.*
import org.jetbrains.kotlin.gradle.tasks.*
import org.gradle.internal.os.OperatingSystem as os

art {
    embeddedModules {
        kit()
    }
    mainClass("ru.art.platform.agent.module.AgentModule")
}


dependencies {
    embedded(project(":api"))
    embedded("org.zeroturnaround", "zt-exec", art.externalDependencyVersionsConfiguration.zeroTurnaroundVersion)
    embedded("org.eclipse.jgit:org.eclipse.jgit:5.5.1.201910021850-r")
}

task("buildDockerImage", type = Exec::class) {
    if (os.current().isWindows) {
        commandLine("cmd", "/c", "docker build --rm -t platform/agent:latest .")
        return@task
    }
    commandLine("docker build --rm -t platform/agent:latest .")
}

tasks["build"].finalizedBy("buildDockerImage")

tasks.withType<KotlinCompile> {
    sourceCompatibility = VERSION_1_8.toString()
    targetCompatibility = VERSION_1_8.toString()

    kotlinOptions {
        jvmTarget = VERSION_1_8.toString()
    }
}

tasks.withType(JavaCompile::class).forEach { task -> task.dependsOn(":api:build") }