package ru.art.platform.service

import reactor.core.publisher.*
import reactor.core.publisher.Flux.*
import ru.art.core.constants.NetworkConstants.*
import ru.art.core.network.selector.PortSelector.*
import ru.art.logging.LoggingModule.*
import ru.art.platform.api.constants.ApIConstants.*
import ru.art.platform.api.mapping.ProjectMapper.*
import ru.art.platform.api.mapping.ProjectRequestMapper.*
import ru.art.platform.api.model.*
import ru.art.platform.constants.CommonConstants.PLATFORM
import ru.art.platform.constants.CommonConstants.PROJECT
import ru.art.platform.constants.DockerConstants.AGENT_MAX_PORT
import ru.art.platform.constants.DockerConstants.AGENT_MIN_PORT
import ru.art.platform.constants.LoggingMessages.CONTAINER_FOR_AGENT_INITIALIZED
import ru.art.platform.service.DockerService.removeAgentContainer
import ru.art.platform.service.DockerService.startAgentContainerIfNeeded
import ru.art.rsocket.communicator.RsocketCommunicator.*
import ru.art.tarantool.dao.TarantoolDao.*


object ProjectService {
    private val logger = loggingModule().getLogger(ProjectService::class.java)

    fun addProject(request: ProjectRequest): Flux<Project> {
        val port = findAvailableTcpPort(AGENT_MIN_PORT, AGENT_MAX_PORT)
        val project = toProject.map(tarantool(PLATFORM).put(PROJECT, fromProjectRequest.map(request)))
        return create<Project> { emitter ->
            emitter.next(project)
            startAgentContainerIfNeeded(project.title, port)
            logger.info(CONTAINER_FOR_AGENT_INITIALIZED(project.title))
            rsocketCommunicator(LOCALHOST, port)
                    .functionId(INITIALIZE_PROJECT)
                    .requestMapper(fromProject)
                    .responseMapper(toProject)
                    .stream<Project, Project>(project)
                    .map { response -> response.responseData }
                    .doOnNext { updatedProject -> tarantool(PLATFORM).put(PROJECT, fromProject.map(updatedProject)) }
                    .doOnComplete { removeAgentContainer(project.title) }
                    .subscribe({ project -> emitter.next(project) }, { error -> emitter.error(error) }, { emitter.complete() })
        }
    }

    fun buildProject(request: BuildRequest) {

    }

    fun deleteProject(request: Long) {
        tarantool(PLATFORM).delete(PROJECT, request)
    }

    fun getProjects(): Set<Project> = tarantool(PLATFORM).selectAll(PROJECT).map(toProject::map).toSet()
}
