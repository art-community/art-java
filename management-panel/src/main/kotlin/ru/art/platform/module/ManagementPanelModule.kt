package ru.art.platform.module

import ru.art.config.extensions.activator.AgileConfigurationsActivator.*
import ru.art.config.extensions.http.*
import ru.art.core.context.Context.*
import ru.art.core.factory.CollectionsFactory.*
import ru.art.entity.PrimitiveMapping.*
import ru.art.entity.Value.*
import ru.art.http.constants.HttpStatus.*
import ru.art.http.server.HttpServer.*
import ru.art.http.server.constants.HttpServerModuleConstants.HttpResourceServiceConstants.*
import ru.art.http.server.interceptor.*
import ru.art.http.server.interceptor.CookieInterceptor.Error.*
import ru.art.http.server.interceptor.HttpServerInterceptor.*
import ru.art.http.server.module.*
import ru.art.http.server.service.HttpResourceService.*
import ru.art.platform.api.mapping.UserAuthorizationRequestMapper.*
import ru.art.platform.api.mapping.UserMapper.*
import ru.art.platform.api.mapping.UserRegistrationRequestMapper.*
import ru.art.platform.constants.CommonConstants.ACCESS_TOKEN
import ru.art.platform.constants.CommonConstants.PLATFORM
import ru.art.platform.constants.CommonConstants.TOKEN
import ru.art.platform.constants.CommonConstants.USER
import ru.art.platform.constants.DbConstants.TOKEN_FIELD_NUM
import ru.art.platform.constants.DbConstants.TOKEN_INDEX_ID
import ru.art.platform.constants.ServiceConstants.CHECK_TOKEN
import ru.art.platform.constants.ServiceConstants.GET_USER
import ru.art.platform.constants.ServiceConstants.LOGIN
import ru.art.platform.constants.ServiceConstants.REGISTER_USER
import ru.art.platform.service.*
import ru.art.platform.service.UserService.checkToken
import ru.art.rsocket.factory.RsocketFunctionPredicateFactory.*
import ru.art.rsocket.function.RsocketServiceFunction.*
import ru.art.rsocket.interceptor.RsocketFilterableInterceptor.*
import ru.art.rsocket.interceptor.RsocketPayloadValueInterceptor.*
import ru.art.rsocket.module.RsocketModule.*
import ru.art.rsocket.server.RsocketServer.*
import ru.art.service.constants.RequestValidationPolicy.*
import ru.art.tarantool.configuration.lua.TarantoolIndexConfiguration.*
import ru.art.tarantool.constants.TarantoolModuleConstants.TarantoolFieldType.*
import ru.art.tarantool.service.TarantoolIndexService.*

object ManagementPanelModule {
    @JvmStatic
    fun main(args: Array<String>) {
        useAgileConfigurations()
        context().loadModule(HttpServerModule(), object : HttpServerAgileConfiguration() {
            override fun getRequestInterceptors(): MutableList<HttpServerInterceptor> = linkedListOf<HttpServerInterceptor>()
                    .also { interceptors ->
                        interceptors.add(intercept(CookieInterceptor
                                .builder()
                                .cookieValidator("token", ::checkToken)
                                .pathFilter { path -> !path.contains("/metrics") }
                                .errorProvider { path -> cookieError(UNAUTHORIZED.code, getStringResource(INDEX_HTML)) }
                                .build()))
                    }
                    .also { interceptors -> interceptors.addAll(super.getRequestInterceptors()) }
        })
        createIndex(PLATFORM, builder()
                .id(TOKEN_INDEX_ID)
                .spaceName(USER)
                .indexName(TOKEN)
                .part(Part.builder().fieldNumber(TOKEN_FIELD_NUM).type(STRING).build())
                .build())
        createIndex(PLATFORM, builder()
                .spaceName(TOKEN)
                .indexName(ACCESS_TOKEN)
                .part(Part.builder().fieldNumber(1).type(STRING).build())
                .build())
        rsocketModule()
                .serverInterceptors
                .add(rsocketInterceptor(byRsocketFunction("registerUser").negate(), intercept { value -> checkToken(asEntity(value).getString("token")) }))
        rsocket(REGISTER_USER)
                .requestMapper(toUserRegistrationRequest)
                .responseMapper(fromUser)
                .validationPolicy(VALIDATABLE)
                .handle(UserService::registerUser)
        rsocket(GET_USER)
                .requestMapper(stringMapper.toModel)
                .responseMapper(fromUser)
                .validationPolicy(VALIDATABLE)
                .handle(UserService::getUser)
        rsocket(LOGIN)
                .requestMapper(toUserAuthorizationRequest)
                .validationPolicy(VALIDATABLE)
                .handle(UserService::login)
        rsocket(CHECK_TOKEN)
                .requestMapper(stringMapper.toModel)
                .validationPolicy(VALIDATABLE)
                .handle(UserService::checkToken)
        startRsocketWebSocketServer()
        startHttpServer().await()
    }
}