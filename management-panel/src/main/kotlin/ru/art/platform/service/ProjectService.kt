package ru.art.platform.service

import reactor.core.publisher.*
import ru.art.platform.api.constants.ApIConstants.*
import ru.art.platform.api.mapping.ProjectMapper.*
import ru.art.platform.api.mapping.ProjectRequestMapper.*
import ru.art.platform.api.model.*
import ru.art.platform.constants.CommonConstants.AGENT
import ru.art.platform.constants.CommonConstants.PLATFORM
import ru.art.platform.constants.CommonConstants.PROJECT
import ru.art.rsocket.communicator.RsocketCommunicator.*
import ru.art.rsocket.module.RsocketModule.*
import ru.art.tarantool.dao.TarantoolDao.*

object ProjectService {
    fun addProject(request: ProjectRequest): Flux<Project> = rsocketCommunicator(rsocketModule().getCommunicationTargetConfiguration(AGENT))
            .functionId(INITIALIZE_PROJECT)
            .requestMapper(fromProject)
            .responseMapper(toProject)
            .stream<Project, Project>(toProject.map(tarantool(PLATFORM).put(PROJECT, fromProjectRequest.map(request))))
            .map { response -> response.responseData }
            .doOnNext { project -> tarantool(PLATFORM).put(PROJECT, fromProject.map(project)) }

    fun deleteProject(request: Long): Unit {
        tarantool(PLATFORM).delete(PROJECT, request)
    }

    fun getProjects(): Set<Project> = tarantool(PLATFORM).selectAll(PROJECT).map(toProject::map).toSet()
}