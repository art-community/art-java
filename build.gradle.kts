import com.jfrog.bintray.gradle.*

plugins {
    `maven-publish`
    id("io.github.art.project") version "1.0.37"
    id("com.jfrog.bintray") version "1.8.4"
}

val bintrayUser: String? by project
val bintrayKey: String? by project

allprojects {
    group = "io.github.art"
    version = "1.0.0"

    repositories {
        jcenter()
        mavenCentral()
    }

    apply(plugin = "io.github.art.project")
    apply(plugin = "com.jfrog.bintray")
    apply(plugin = "maven-publish")

    art {
        idea()
        lombok()
        tests()
    }

    afterEvaluate {
        val jar: Jar by tasks
        val sourceJar = task("sourceJar", type = Jar::class) {
            archiveClassifier.set("sources")
            from(sourceSets.main.get().allJava)
        }

        publishing {
            publications {
                create<MavenPublication>(project.name) {
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
            user = bintrayUser ?: ""
            key = bintrayKey ?: ""
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
            tasks["bintrayUpload"].dependsOn(tasks["generatePomFileFor${name.capitalize()}Publication"], jar, sourceJar)
        }
    }
}

afterEvaluate {
    tasks["bintrayUpload"].enabled = false
}