import com.jfrog.bintray.gradle.*

plugins {
    `maven-publish`
    id("ru.art.project") version "1.0.16"
    id("com.jfrog.bintray") version ("1.8.4")
}

allprojects {
    group = "io.github.art"
    version = "1.0.0"

    repositories {
        jcenter()
        mavenCentral()
    }
    apply(plugin = "ru.art.project")
    apply(plugin = "com.jfrog.bintray")
    apply(plugin = "maven-publish")

    art {
        idea()
        lombok()
    }

    val sourceJar = task("sourceJar", type = Jar::class) {
        archiveClassifier.set("sources")
        from(sourceSets.main.get().allJava)
    }

    publishing {
        publications {
            create<MavenPublication>(project.name) {
                val jar: Jar by tasks
                artifact(jar)
                artifact(sourceJar)
                groupId = rootProject.group as String
                artifactId = project.name
                version = rootProject.version as String
                pom {
                    packaging = "jar"
                    description.set(project.name)
                }
            }
        }
    }

    bintray {
        user = "antonb"
        key = "7729eaea5e7246eb64bc6536a2b0630694bbf5d1"
        publish = true
        override = true
        setPublications(project.name)
        pkg(delegateClosureOf<BintrayExtension.PackageConfig> {
            repo = "art"
            name = rootProject.group as String
            userOrg = "art-community"
            websiteUrl = "https://github.com/art-community/art"
            vcsUrl = "https://github.com/art-community/art"
            setLabels("art", "kotlin", "java", "rsocket", "tarantool", "grpc", "protobuf", "rocksdb", "http", "tomcat")
            setLicenses("Apache-2.0")
        })
        tasks["bintrayUpload"].dependsOn(tasks["generatePomFileFor${name.capitalize()}Publication"], sourceJar)
    }
}

afterEvaluate {
    tasks["bintrayUpload"].enabled = false
}