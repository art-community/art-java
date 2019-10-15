package ru.art.platform.agent.state

import reactor.core.publisher.*
import ru.art.platform.api.model.*
import java.util.concurrent.*

object AgentModuleState {
    private val projectEmitter = ConcurrentHashMap<Long, FluxSink<Project>>()

    fun subscribeOnProject(id: Long, emitter: FluxSink<Project>) {
        projectEmitter[id] = emitter
    }

    fun onProject(project: Project) {
        projectEmitter[project.id]?.next(project)
    }
}