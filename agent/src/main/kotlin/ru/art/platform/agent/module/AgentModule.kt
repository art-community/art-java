package ru.art.platform.agent.module

import ru.art.config.extensions.activator.AgileConfigurationsActivator.*
import ru.art.rsocket.communicator.RsocketCommunicator.*
import ru.art.rsocket.module.RsocketModule.*

object AgentModule {
    @JvmStatic
    fun main(args: Array<String>) {
        loadModules()
        rsocketCommunicator(rsocketModule().getCommunicationTargetConfiguration("managementPanel"))
                .functionId()
    }

    private fun loadModules() {
        useAgileConfigurations()
    }
}