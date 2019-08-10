art {
    providedModules {
        applicationCore()
        applicationEntity()
        applicationLogging()
        applicationService()
        applicationGrpcClient()
    }

    generator {
        packageName = "ru.art.state.api"
    }

    dependencySubstitution {
        substituteWithCode(project.group as String, "application-generator")
    }
}