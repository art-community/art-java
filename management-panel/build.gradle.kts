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

}


tasks.withType<KotlinCompile> {
    sourceCompatibility = VERSION_1_8.toString()
    targetCompatibility = VERSION_1_8.toString()

    kotlinOptions {
        jvmTarget = VERSION_1_8.toString()
    }
}

task("stop") {
    group = "deployment"
    doLast {
        exec {
            commandLine = listOf("docker", "kill", "management-panel")
        }
    }
}
val DEPLOYMENT_DIRECTORY = "D:\\Development\\Docker\\Volume\\platform\\management-panel"
task("buildImage") {
    group = "deployment"
    dependsOn("build")
    doLast {
        copy {
            from("build/libs")
            into(DEPLOYMENT_DIRECTORY)
        }
        copy {
            from("src/main/resources")
            into(DEPLOYMENT_DIRECTORY)
        }
        exec {
            commandLine = listOf("docker", "build", "--rm", "-t", "platform/management-panel:latest", ".")
        }
    }
}


task("start") {
    group = "deployment"
    doLast {
        exec {
            commandLine = listOf("docker",
                    "run", "-d", "--rm",
                    "-p", "8080:8080",
                    "-p", "9001:9001",
                    "-p", "3301:3301",
                    "-v", "/host_mnt/d/Development/Docker/Volume/platform/management-panel:/management-panel:rw",
                    "--name", "management-panel",
                    "platform/management-panel:latest")
        }
    }
}

tasks.withType(JavaCompile::class).forEach { task -> task.dependsOn(":api:build") }