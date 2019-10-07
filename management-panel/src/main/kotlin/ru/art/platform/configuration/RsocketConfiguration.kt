package ru.art.platform.configuration

import io.rsocket.plugins.*
import ru.art.config.extensions.rsocket.*
import ru.art.core.factory.CollectionsFactory.*
import ru.art.entity.Value.*
import ru.art.platform.constants.CommonConstants.TOKEN
import ru.art.platform.constants.ServiceConstants.AUTHORIZE
import ru.art.platform.constants.ServiceConstants.REGISTER_USER
import ru.art.platform.service.UserService.authenticate
import ru.art.rsocket.factory.RsocketFunctionPredicateFactory.*
import ru.art.rsocket.interceptor.RsocketFilterableInterceptor.*
import ru.art.rsocket.interceptor.RsocketPayloadValueInterceptor.*

class RsocketConfiguration : RsocketAgileConfiguration() {
    override fun getServerInterceptors(): MutableList<RSocketInterceptor> = linkedListOf<RSocketInterceptor>().apply {
        add(rsocketInterceptor((byRsocketFunction(REGISTER_USER).or(byRsocketFunction(AUTHORIZE))).negate(),
                intercept { value -> authenticate(asEntity(value).getString(TOKEN)) }))
    }
}