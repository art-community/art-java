package ru.art.platform.module

import ru.art.config.extensions.activator.AgileConfigurationsActivator.*
import ru.art.entity.PrimitiveMapping.*
import ru.art.http.server.HttpServer.*
import ru.art.http.server.module.*
import ru.art.platform.api.mapping.UserAuthorizationRequestMapper.*
import ru.art.platform.api.mapping.UserMapper.*
import ru.art.platform.api.mapping.UserRegistrationRequestMapper.*
import ru.art.platform.configuration.*
import ru.art.platform.constants.CommonConstants.PLATFORM
import ru.art.platform.constants.CommonConstants.TOKEN
import ru.art.platform.constants.CommonConstants.USER
import ru.art.platform.constants.DbConstants.TOKEN_FIELD_NUM
import ru.art.platform.constants.DbConstants.TOKEN_INDEX_ID
import ru.art.platform.constants.ServiceConstants.AUTHENTICATE
import ru.art.platform.constants.ServiceConstants.AUTHORIZE
import ru.art.platform.constants.ServiceConstants.GET_USER
import ru.art.platform.constants.ServiceConstants.REGISTER_USER
import ru.art.platform.service.*
import ru.art.rsocket.function.RsocketServiceFunction.*
import ru.art.rsocket.module.*
import ru.art.rsocket.server.RsocketServer.*
import ru.art.service.constants.RequestValidationPolicy.*
import ru.art.tarantool.configuration.lua.TarantoolIndexConfiguration.*
import ru.art.tarantool.constants.TarantoolModuleConstants.TarantoolFieldType.*
import ru.art.tarantool.service.TarantoolIndexService.*

object ManagementPanelModule {
    @JvmStatic
    fun main(args: Array<String>) {
        loadModules()
        createIndexes()
        registerFunctions()
        startServers()
    }

    private fun loadModules() {
        useAgileConfigurations()
                .loadModule(HttpServerModule(), HttpServerConfiguration())
                .loadModule(RsocketModule(), RsocketConfiguration())
    }

    private fun createIndexes() {
        createIndex(PLATFORM, builder()
                .id(TOKEN_INDEX_ID)
                .spaceName(USER)
                .indexName(TOKEN)
                .part(Part.builder().fieldNumber(TOKEN_FIELD_NUM).type(STRING).build())
                .build())
        createIndex(PLATFORM, builder()
                .id(TOKEN_INDEX_ID)
                .spaceName(TOKEN)
                .indexName(TOKEN)
                .part(Part.builder().fieldNumber(TOKEN_FIELD_NUM).type(STRING).build())
                .build())
    }

    private fun registerFunctions() {
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
        rsocket(AUTHORIZE)
                .requestMapper(toUserAuthorizationRequest)
                .responseMapper(stringMapper.fromModel)
                .validationPolicy(VALIDATABLE)
                .handle(UserService::authorize)
        rsocket(AUTHENTICATE)
                .requestMapper(stringMapper.toModel)
                .validationPolicy(VALIDATABLE)
                .handle(UserService::authenticate)
    }


    private fun startServers() {
        startRsocketWebSocketServer()
        startHttpServer().await()
    }
}