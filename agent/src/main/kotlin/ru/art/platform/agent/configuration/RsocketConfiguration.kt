package ru.art.platform.agent.configuration

import ru.art.config.extensions.rsocket.*
import ru.art.platform.agent.constants.ErrorMessages.PORT_NOT_STATED
import ru.art.platform.agent.exception.*
import ru.art.platform.api.constants.CommonConstants.*
import java.lang.System.*

class RsocketConfiguration : RsocketAgileConfiguration() {
    override fun getServerTcpPort(): Int = getenv()[PORT_ENVIRONMENT_PROPERTY]
            ?.toInt() ?: throw AgentException(PORT_NOT_STATED)
}