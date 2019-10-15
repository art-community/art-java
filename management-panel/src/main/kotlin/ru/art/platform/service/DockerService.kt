package ru.art.platform.service

import com.github.dockerjava.api.model.HostConfig.*
import com.github.dockerjava.api.model.PortBinding.*
import com.github.dockerjava.core.DockerClientBuilder.*
import com.github.dockerjava.core.command.*
import ru.art.platform.constants.DockerConstants.AGENT_CONTAINER_NAME
import ru.art.platform.constants.DockerConstants.AGENT_IMAGE
import ru.art.platform.constants.DockerConstants.ContainerStates.*
import ru.art.platform.constants.DockerConstants.DOCKER_URL

object DockerService {
    fun startAgentContainerIfNeeded(projectName: String): Unit = with(getInstance(DOCKER_URL).build()) {
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
        createContainerCmd(AGENT_IMAGE)
                .withHostConfig(newHostConfig()
                        .withPortBindings(parse("10001:10001"))
                        .withAutoRemove(true))
                .withName(AGENT_CONTAINER_NAME(projectName))
                .exec()
                .id
                .apply {
                    val resultCallback = WaitContainerResultCallback()
                    startContainerCmd(this).exec()
                    waitContainerCmd(this).exec(resultCallback)
                    resultCallback.awaitCompletion()
                }
    }
}