package ru.art.platform.agent.service

import org.apache.logging.log4j.io.IoBuilder.*
import org.eclipse.jgit.api.Git.*
import ru.art.logging.LoggingModule.*
import ru.art.platform.agent.constants.BuildFiles.BUILD_GRADLE
import ru.art.platform.agent.constants.BuildFiles.BUILD_GRADLE_KTS
import ru.art.platform.agent.constants.BuildFiles.PACKAGE_JSON_FILE
import ru.art.platform.agent.constants.BuildFiles.YARN_LOCK_FILES
import ru.art.platform.agent.constants.Technologies.GRADLE
import ru.art.platform.agent.constants.Technologies.NPM
import ru.art.platform.agent.constants.Technologies.YARN
import ru.art.platform.agent.state.AgentModuleState.emitProjectUpdate
import ru.art.platform.api.constants.ApIConstants.ProjectState.*
import ru.art.platform.api.model.*
import java.nio.file.Files.*
import java.nio.file.Paths.*


object ProjectService {
    private val LOGGER_OUTPUT_STREAM = forLogger(loggingModule()
            .getLogger(ProjectService::class.java))
            .buildOutputStream()
    private val logger = loggingModule().getLogger(ProjectService::class.java)

    fun initializeProject(project: Project) {
        val path = get(project.title)
        createDirectories(path)
        cloneRepository()
                .setURI(project.gitUrl)
                .setDirectory(path.toFile())
                .call()
        val technologies = mutableSetOf<String>()
        path.toFile()
                .walkTopDown()
                .filter { file -> file.isFile }
                .forEach { file ->
                    when (file.name) {
                        PACKAGE_JSON_FILE -> technologies += NPM
                        BUILD_GRADLE -> technologies += GRADLE
                        BUILD_GRADLE_KTS -> technologies += GRADLE
                        YARN_LOCK_FILES -> technologies += YARN
                    }
                }
        emitProjectUpdate(project.toBuilder().technologies(technologies).state(INITIALIZED).build())
    }
}