package ru.art.platform.service

import com.github.dockerjava.api.model.*
import com.github.dockerjava.api.model.HostConfig.*
import com.github.dockerjava.api.model.PortBinding.*
import com.github.dockerjava.core.DockerClientBuilder.*
import com.github.dockerjava.core.command.*
import ru.art.core.constants.CharConstants.*
import ru.art.logging.LoggingModule.*
import ru.art.platform.api.constants.CommonConstants.*
import ru.art.platform.constants.DockerConstants.AGENT_CONTAINER_NAME
import ru.art.platform.constants.DockerConstants.AGENT_CONTAINER_WAITING_TIME_SECONDS
import ru.art.platform.constants.DockerConstants.AGENT_IMAGE
import ru.art.platform.constants.DockerConstants.DOCKER_URL
import ru.art.platform.constants.LoggingMessages.CREATED_AGENT_CONTAINER
import ru.art.platform.constants.LoggingMessages.KILLED_AGENT_CONTAINER
import ru.art.platform.constants.LoggingMessages.REMOVED_AGENT_CONTAINER
import ru.art.platform.constants.LoggingMessages.STARTED_AGENT_CONTAINER
import java.util.concurrent.*

object DockerService {
    private val logger = loggingModule().getLogger(DockerService::class.java)

    fun startAgentContainerIfNeeded(projectTitle: String, port: Int): Unit = with(getInstance(DOCKER_URL).build()) {
        removeAgentContainer(projectTitle)
        val containerId = createContainerCmd(AGENT_IMAGE)
                .withHostConfig(newHostConfig()
                        .withPrivileged(true)
                        .withAutoRemove(true)
                        .withPortBindings(parse("$port$COLON$port")))
                .withEnv("$PORT_ENVIRONMENT_PROPERTY$EQUAL$port")
                .withExposedPorts(ExposedPort.tcp(port))
                .withName(AGENT_CONTAINER_NAME(projectTitle))
                .exec()
                .id
        logger.info(CREATED_AGENT_CONTAINER(AGENT_CONTAINER_NAME(projectTitle), containerId))
        startContainerCmd(containerId).exec()
        logger.info(STARTED_AGENT_CONTAINER(AGENT_CONTAINER_NAME(projectTitle), containerId))
        WaitContainerResultCallback().
                let(waitContainerCmd(containerId)::exec)
                .apply { runCatching { awaitStarted(AGENT_CONTAINER_WAITING_TIME_SECONDS, TimeUnit.SECONDS) } }
    }

    fun removeAgentContainer(projectTitle: String): Unit = with(getInstance(DOCKER_URL).build()) {
        runCatching {
            killContainerCmd(AGENT_CONTAINER_NAME(projectTitle)).exec()
            logger.info(KILLED_AGENT_CONTAINER(AGENT_CONTAINER_NAME(projectTitle)))
            removeContainerCmd(AGENT_CONTAINER_NAME(projectTitle)).exec()
            logger.info(REMOVED_AGENT_CONTAINER(AGENT_CONTAINER_NAME(projectTitle)))
        }
    }
}