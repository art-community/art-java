package ru.art.platform.service

import reactor.core.publisher.*
import ru.art.core.constants.NetworkConstants.*
import ru.art.core.network.selector.PortSelector.*
import ru.art.logging.LoggingModule.*
import ru.art.platform.api.constants.ApIConstants.*
import ru.art.platform.api.mapping.ProjectMapper.*
import ru.art.platform.api.mapping.ProjectRequestMapper.*
import ru.art.platform.api.model.*
import ru.art.platform.constants.*
import ru.art.platform.constants.CommonConstants.PLATFORM
import ru.art.platform.constants.CommonConstants.PROJECT
import ru.art.platform.constants.DockerConstants.AGENT_MAX_PORT
import ru.art.platform.constants.DockerConstants.AGENT_MIN_PORT
import ru.art.platform.service.DockerService.killAgentContainer
import ru.art.platform.service.DockerService.startAgentContainerIfNeeded
import ru.art.rsocket.communicator.RsocketCommunicator.*
import ru.art.tarantool.dao.TarantoolDao.*


object ProjectService {
    private val logger = loggingModule().getLogger(ProjectService::class.java)

    fun addProject(request: ProjectRequest): Flux<Project> {
        val project = toProject.map(tarantool(PLATFORM).put(PROJECT, fromProjectRequest.map(request)))
        val port = findAvailableTcpPort(AGENT_MIN_PORT, AGENT_MAX_PORT)
        startAgentContainerIfNeeded(project.title, port)
        logger.info(LoggingMessages.CONTAINER_FOR_AGENT_INITIALIZED(project.title))
        return rsocketCommunicator(LOCALHOST, port)
                .functionId(INITIALIZE_PROJECT)
                .requestMapper(fromProject)
                .responseMapper(toProject)
                .stream<Project, Project>(project)
                .map { response -> response.responseData }
                .doOnNext { updatedProject -> tarantool(PLATFORM).put(PROJECT, fromProject.map(updatedProject)) }
                .doOnComplete { killAgentContainer(project.title  ) }
    }

    fun deleteProject(request: Long) {
        tarantool(PLATFORM).delete(PROJECT, request)
    }

    fun getProjects(): Set<Project> = tarantool(PLATFORM).selectAll(PROJECT).map(toProject::map).toSet()
}
