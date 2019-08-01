import ru.adk.gradle.plugin.configuration.SettingsConfiguration

buildscript {
    val artifactory_user: String by settings
    val artifactory_contextUrl: String by settings
    val artifactory_password: String by settings

    repositories {
        maven {
            url = uri("$artifactory_contextUrl/gradle-virtual")
            credentials {
                username = artifactory_user
                password = artifactory_password
            }
        }
    }

    dependencies {
        classpath("ru.adk:application-gradle-plugin:1.+")
    }
}

apply(plugin = "adkSettings")
the<SettingsConfiguration>().addCurrentPath()