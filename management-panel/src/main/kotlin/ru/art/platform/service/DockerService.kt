package ru.art.platform.service

import com.github.dockerjava.api.model.*
import com.github.dockerjava.api.model.HostConfig.*
import com.github.dockerjava.api.model.PortBinding.*
import com.github.dockerjava.core.DockerClientBuilder.*
import com.github.dockerjava.core.command.*
import ru.art.core.constants.CharConstants.*
import ru.art.platform.api.constants.CommonConstants.*
import ru.art.platform.constants.DockerConstants.AGENT_CONTAINER_NAME
import ru.art.platform.constants.DockerConstants.AGENT_IMAGE
import ru.art.platform.constants.DockerConstants.DOCKER_URL
import java.util.concurrent.*

object DockerService {
    fun startAgentContainerIfNeeded(projectName: String, port: Int): Unit = with(getInstance(DOCKER_URL).build()) {
        killAgentContainer(projectName)
        val containerId = createContainerCmd(AGENT_IMAGE)
                .withHostConfig(newHostConfig()
                        .withPrivileged(true)
                        .withAutoRemove(true)
                        .withPortBindings(parse("$port$COLON$port")))
                .withEnv("$PORT_ENVIRONMENT_PROPERTY$EQUAL$port")
                .withExposedPorts(ExposedPort.tcp(port))
                .withName(AGENT_CONTAINER_NAME(projectName))
                .exec()
                .id
        startContainerCmd(containerId).exec()
        WaitContainerResultCallback().let(waitContainerCmd(containerId)::exec).apply { runCatching { awaitStarted(1, TimeUnit.SECONDS) } }
    }

    fun killAgentContainer(projectName: String): Unit = with(getInstance(DOCKER_URL).build()) {
        runCatching {
            killContainerCmd(AGENT_CONTAINER_NAME(projectName))
            removeContainerCmd(AGENT_CONTAINER_NAME(projectName))
        }
    }
}