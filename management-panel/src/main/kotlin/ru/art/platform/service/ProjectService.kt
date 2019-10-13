package ru.art.platform.service

import ru.art.platform.api.mapping.ProjectMapper.*
import ru.art.platform.api.mapping.ProjectRequestMapper.*
import ru.art.platform.api.model.*
import ru.art.platform.constants.CommonConstants.PLATFORM
import ru.art.platform.constants.CommonConstants.PROJECT
import ru.art.tarantool.dao.TarantoolDao.*

object ProjectService {
    fun addProject(request: ProjectRequest): Project = toProject.map(tarantool(PLATFORM).put(PROJECT, fromProjectRequest.map(request)))

    fun deleteProject(request: Long): Unit {
        tarantool(PLATFORM).delete(PROJECT, request)
    }

    fun getProjects(): Set<Project> = tarantool(PLATFORM).selectAll(PROJECT).map(toProject::map).toSet()
}