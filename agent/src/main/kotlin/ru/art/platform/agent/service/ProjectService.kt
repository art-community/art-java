package ru.art.platform.agent.service

import org.apache.logging.log4j.io.IoBuilder.*
import org.eclipse.jgit.api.*
import org.zeroturnaround.exec.*
import ru.art.logging.LoggingModule.*
import ru.art.platform.api.model.*
import ru.art.tarantool.initializer.*



object ProjectService {
    private val LOGGER_OUTPUT_STREAM = forLogger(loggingModule()
            .getLogger(TarantoolInitializer::class.java))
            .buildOutputStream()
    private val logger = loggingModule().getLogger(TarantoolInitializer::class.java)

    fun initializeProject(project: Project) {
        val git = Git.cloneRepository()
                .setURI(project.gitUrl)
                .call()

        ProcessExecutor()
                .command("apt-get", "-qqy", "--no-install-recommends", "update")
                .redirectOutput(LOGGER_OUTPUT_STREAM)
                .redirectError(LOGGER_OUTPUT_STREAM)
                .start()
                .process
                .waitFor()
        ProcessExecutor()
                .command("apt-get", "-qqy", "--no-install-recommends", "install", "nginx")
                .redirectOutput(LOGGER_OUTPUT_STREAM)
                .redirectError(LOGGER_OUTPUT_STREAM)
                .start()
                .process
                .waitFor()
    }
}