package ru.art.platform.agent.module

import reactor.core.publisher.*
import reactor.core.publisher.Flux.*
import ru.art.config.extensions.activator.AgileConfigurationsActivator.*
import ru.art.config.extensions.rsocket.*
import ru.art.platform.agent.constants.CommonConstants.PORT_ENV_PROPERTIES
import ru.art.platform.agent.exception.*
import ru.art.platform.agent.service.ProjectService.initializeProject
import ru.art.platform.agent.state.AgentModuleState.subscribeOnProject
import ru.art.platform.api.constants.ApIConstants.*
import ru.art.platform.api.mapping.ProjectMapper.*
import ru.art.platform.api.model.*
import ru.art.reactive.service.constants.ReactiveServiceModuleConstants.ReactiveMethodProcessingMode.*
import ru.art.rsocket.function.RsocketServiceFunction.*
import ru.art.rsocket.module.*
import ru.art.rsocket.server.RsocketServer.*
import java.lang.System.*

object AgentModule {
    @JvmStatic
    fun main(args: Array<String>) {
        loadModules()
        registerFunctions()
        startRsocketTcpServer().await()
    }

    private fun loadModules() {
        useAgileConfigurations()
                .loadModule(RsocketModule(), object: RsocketAgileConfiguration() {
                    override fun getServerTcpPort(): Int {
                        return getenv()[PORT_ENV_PROPERTIES]?.toInt() ?: throw AgentException("ENV Property 'PORT' not stated")
                    }
                })
    }

    private fun registerFunctions() {
        rsocket(INITIALIZE_PROJECT)
                .requestMapper(toProject)
                .responseProcessingMode(REACTIVE)
                .responseMapper(fromProject)
                .handle<Project, Flux<Project>> { project ->
                    create<Project> { emitter ->
                        subscribeOnProject(project.id, emitter)
                        initializeProject(project)
                    }
                }
    }
}