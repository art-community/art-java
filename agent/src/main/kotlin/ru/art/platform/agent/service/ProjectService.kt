package ru.art.platform.agent.service

import org.apache.logging.log4j.io.IoBuilder.*
import ru.art.logging.LoggingModule.*
import ru.art.platform.api.model.*


object ProjectService {
    private val LOGGER_OUTPUT_STREAM = forLogger(loggingModule()
            .getLogger(ProjectService::class.java))
            .buildOutputStream()
    private val logger = loggingModule().getLogger(ProjectService::class.java)

    fun initializeProject(project: Project) {

    }
}