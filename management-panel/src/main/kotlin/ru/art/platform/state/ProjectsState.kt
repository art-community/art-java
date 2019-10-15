package ru.art.platform.state

import reactor.core.publisher.*
import ru.art.platform.api.model.*
import java.util.concurrent.*

object ProjectsState {
    val pendingInitializationProjects = ConcurrentHashMap<Long, Project>()
    val projectsEmitter = ConcurrentHashMap<Long, FluxSink<Project>>()

    fun emitProjectUpdate(project: Project): Unit {
        projectsEmitter[project.id]?.next(project)
    }
}