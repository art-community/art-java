package ru.art.platform.service

import reactor.core.publisher.*
import ru.art.core.network.selector.PortSelector.*
import ru.art.platform.api.constants.ApIConstants.*
import ru.art.platform.api.mapping.ProjectMapper.*
import ru.art.platform.api.mapping.ProjectRequestMapper.*
import ru.art.platform.api.model.*
import ru.art.platform.constants.CommonConstants.PLATFORM
import ru.art.platform.constants.CommonConstants.PROJECT
import ru.art.platform.service.DockerService.killAgentContainer
import ru.art.platform.service.DockerService.startAgentContainerIfNeeded
import ru.art.rsocket.communicator.RsocketCommunicator.*
import ru.art.tarantool.dao.TarantoolDao.*


object ProjectService {
    fun addProject(request: ProjectRequest): Flux<Project> {
        val project = toProject.map(tarantool(PLATFORM).put(PROJECT, fromProjectRequest.map(request)))
        val port = findAvailableTcpPort(9000, 11000)
        startAgentContainerIfNeeded(project.title, port)
        return rsocketCommunicator("185.155.19.36", port)
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
