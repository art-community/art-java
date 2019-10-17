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
import ru.art.platform.service.DockerService.startAgentContainerIfNeeded
import ru.art.rsocket.communicator.RsocketCommunicator.*
import ru.art.tarantool.dao.TarantoolDao.*


object ProjectService {
    private val logger = loggingModule().getLogger(ProjectService::class.java)

    fun addProject(request: ProjectRequest): Flux<Project> = create<Project> { emitter ->
        val project = toProject.map(tarantool(PLATFORM).put(PROJECT, fromProjectRequest.map(request)))
        emitter.next(project)
        val port = findAvailableTcpPort(AGENT_MIN_PORT, AGENT_MAX_PORT)
        startAgentContainerIfNeeded(project.title, port)
        logger.info(CONTAINER_FOR_AGENT_INITIALIZED(project.title))
        rsocketCommunicator(LOCALHOST, port)
                .functionId(INITIALIZE_PROJECT)
                .requestMapper(fromProject)
                .responseMapper(toProject)
                .stream<Project, Project>(project)
                .map { response -> response.responseData }
                .doOnNext { updatedProject -> tarantool(PLATFORM).put(PROJECT, fromProject.map(updatedProject)) }
                .subscribe({ updatedProject -> emitter.next(updatedProject) }, { error -> emitter.error(error) }, { emitter.complete() })
    }

    fun buildProject(request: BuildRequest) {
        val project = tarantool(PLATFORM).get(PROJECT, setOf(request.projectId)).map(toProject::map)
        if (!project.isPresent) {
            return
        }
        var port = findAvailableTcpPort(AGENT_MIN_PORT, AGENT_MAX_PORT)
        val foundContainer = tarantool(PLATFORM).getByIndex("container", "project", setOf(project.get().title))
        if (foundContainer.isPresent) {
            port = foundContainer.get().getInt("port")
        }
        startAgentContainerIfNeeded(project.get().title, port)
        logger.info(CONTAINER_FOR_AGENT_INITIALIZED(project.get().title))
        rsocketCommunicator(LOCALHOST, port)
                .functionId(BUILD_PROJECT)
                .requestMapper(fromProject)
                .call(project.get())
                .subscribe()
    }

    fun deleteProject(request: Long) {
        tarantool(PLATFORM).delete(PROJECT, request)
    }

    fun getProjects(): Set<Project> = tarantool(PLATFORM).selectAll(PROJECT).map(toProject::map).toSet()
}
