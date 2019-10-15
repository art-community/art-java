package ru.art.platform.service

import com.github.dockerjava.api.model.*
import com.github.dockerjava.api.model.HostConfig.*
import com.github.dockerjava.api.model.PortBinding.*
import com.github.dockerjava.core.DockerClientBuilder.*
import com.github.dockerjava.core.command.*
import ru.art.core.wrapper.ExceptionWrapper.*
import ru.art.platform.constants.DockerConstants.AGENT_CONTAINER_NAME
import ru.art.platform.constants.DockerConstants.AGENT_IMAGE
import ru.art.platform.constants.DockerConstants.ContainerStates.*
import ru.art.platform.constants.DockerConstants.DOCKER_URL
import java.util.concurrent.*

object DockerService {
    fun startAgentContainerIfNeeded(projectName: String, port: Int): Unit = with(getInstance(DOCKER_URL).build()) {
        if (listContainersCmd()
                        .withNameFilter(listOf(AGENT_CONTAINER_NAME(projectName)))
                        .exec()
                        .stream()
                        .map { container -> valueOf(container.state) }
                        .anyMatch { state -> state in listOf(running) }) {
            return
        }
        runCatching {
            killContainerCmd(AGENT_CONTAINER_NAME(projectName))
            removeContainerCmd(AGENT_CONTAINER_NAME(projectName))
        }
        val resultCallback = WaitContainerResultCallback()
        val containerId = createContainerCmd(AGENT_IMAGE)
                .withHostConfig(newHostConfig()
                        .withPrivileged(true)
                        .withAutoRemove(true)
                        .withPortBindings(parse("10001:10001"), parse("$port:$port")))
                .withEnv("PORT=$port")
                .withExposedPorts(ExposedPort.tcp(10001), ExposedPort.tcp(port))
                .withName(AGENT_CONTAINER_NAME(projectName))
                .exec()
                .id
        startContainerCmd(containerId).exec()
        waitContainerCmd(containerId).exec(resultCallback)
        ignoreException {
            resultCallback.awaitStarted(1, TimeUnit.SECONDS)
        }

    }

    fun killAgentContainer(projectName: String): Unit = with(getInstance(DOCKER_URL).build()) {
        runCatching {
            killContainerCmd(AGENT_CONTAINER_NAME(projectName))
            removeContainerCmd(AGENT_CONTAINER_NAME(projectName))
        }
    }
}