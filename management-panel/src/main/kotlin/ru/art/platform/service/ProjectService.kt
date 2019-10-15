package ru.art.platform.service

import reactor.core.publisher.*
import reactor.core.publisher.Flux.*
import ru.art.platform.api.mapping.ProjectMapper.*
import ru.art.platform.api.mapping.ProjectRequestMapper.*
import ru.art.platform.api.model.*
import ru.art.platform.constants.CommonConstants.PLATFORM
import ru.art.platform.constants.CommonConstants.PROJECT
import ru.art.platform.service.DockerService.startAgentContainerIfNeeded
import ru.art.platform.state.*
import ru.art.platform.state.ProjectsState.emitProjectUpdate
import ru.art.platform.state.ProjectsState.pendingInitializationProjects
import ru.art.platform.state.ProjectsState.projectsEmitter
import ru.art.tarantool.dao.TarantoolDao.*


object ProjectService {
    fun addProject(request: ProjectRequest): Flux<Project> = with(toProject.map(tarantool(PLATFORM).put(PROJECT, fromProjectRequest.map(request)))) {
        pendingInitializationProjects[id] = this
        startAgentContainerIfNeeded(title)
        create { emitter -> projectsEmitter[id] = emitter }
    }

    fun updateProject(project: Project): Unit = toProject.map(tarantool(PLATFORM).put(PROJECT, fromProject.map(project))).let(ProjectsState::emitProjectUpdate)

    fun deleteProject(request: Long) {
        tarantool(PLATFORM).delete(PROJECT, request)
    }

    fun getProjects(): Set<Project> = tarantool(PLATFORM).selectAll(PROJECT).map(toProject::map).toSet()
}
