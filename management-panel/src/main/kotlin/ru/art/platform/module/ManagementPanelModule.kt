package ru.art.platform.module

import reactor.core.publisher.*
import ru.art.config.extensions.activator.AgileConfigurationsActivator.*
import ru.art.entity.CollectionMapping.*
import ru.art.entity.PrimitiveMapping.*
import ru.art.http.server.HttpServer.*
import ru.art.http.server.module.*
import ru.art.platform.api.mapping.ProjectMapper.*
import ru.art.platform.api.mapping.ProjectRequestMapper.*
import ru.art.platform.api.mapping.UserAuthorizationRequestResponseMapper.UserAuthorizationRequestMapper.*
import ru.art.platform.api.mapping.UserAuthorizationRequestResponseMapper.UserAuthorizationResponseMapper.*
import ru.art.platform.api.model.*
import ru.art.platform.configuration.*
import ru.art.platform.constants.CommonConstants.NAME
import ru.art.platform.constants.CommonConstants.NAME_PASSWORD
import ru.art.platform.constants.CommonConstants.PLATFORM
import ru.art.platform.constants.CommonConstants.PROJECT
import ru.art.platform.constants.CommonConstants.TOKEN
import ru.art.platform.constants.CommonConstants.USER
import ru.art.platform.constants.DbConstants.PROJECT_NAME_FIELD_NUM
import ru.art.platform.constants.DbConstants.PROJECT_NAME_INDEX_ID
import ru.art.platform.constants.DbConstants.USER_NAME_FIELD_NUM
import ru.art.platform.constants.DbConstants.USER_PASSWORD_FIELD_NUM
import ru.art.platform.constants.DbConstants.USER_TOKEN_FIELD_NUM
import ru.art.platform.constants.DbConstants.USER_TOKEN_INDEX_ID
import ru.art.platform.constants.ServiceConstants.ADD_PROJECT
import ru.art.platform.constants.ServiceConstants.AUTHENTICATE
import ru.art.platform.constants.ServiceConstants.AUTHORIZE
import ru.art.platform.constants.ServiceConstants.DELETE_PROJECT
import ru.art.platform.constants.ServiceConstants.GET_PROJECTS
import ru.art.platform.service.*
import ru.art.reactive.service.constants.ReactiveServiceModuleConstants.ReactiveMethodProcessingMode.*
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
                .id(USER_TOKEN_INDEX_ID)
                .spaceName(USER)
                .indexName(NAME_PASSWORD)
                .part(Part.builder().fieldNumber(USER_NAME_FIELD_NUM).type(STRING).build())
                .part(Part.builder().fieldNumber(USER_PASSWORD_FIELD_NUM).type(STRING).build())
                .build())
        createIndex(PLATFORM, builder()
                .id(USER_TOKEN_INDEX_ID)
                .spaceName(TOKEN)
                .indexName(TOKEN)
                .part(Part.builder().fieldNumber(USER_TOKEN_FIELD_NUM).type(STRING).build())
                .build())
        createIndex(PLATFORM, builder()
                .id(PROJECT_NAME_INDEX_ID)
                .spaceName(PROJECT)
                .indexName(NAME)
                .part(Part.builder().fieldNumber(PROJECT_NAME_FIELD_NUM).type(STRING).build())
                .build())
    }

    private fun registerFunctions() {
//        rsocket(REGISTER_USER)
//                .requestMapper(toUserRegistrationRequest)
//                .responseMapper(fromUserRegistrationResponse)
//                .validationPolicy(VALIDATABLE)
//                .handle(UserService::registerUser)
        rsocket(AUTHORIZE)
                .requestMapper(toUserAuthorizationRequest)
                .responseMapper(fromUserAuthorizationResponse)
                .validationPolicy(VALIDATABLE)
                .handle(UserService::authorize)
        rsocket(AUTHENTICATE)
                .requestMapper(stringMapper.toModel)
                .responseMapper(boolMapper.fromModel)
                .validationPolicy(NOT_NULL)
                .handle(UserService::authenticate)
        rsocket(ADD_PROJECT)
                .requestMapper(toProjectRequest)
                .validationPolicy(VALIDATABLE)
                .responseMapper(fromProject)
                .responseProcessingMode(REACTIVE)
                .handle<ProjectRequest, Flux<Project>>(ProjectService::addProject)
        rsocket(GET_PROJECTS)
                .responseMapper(collectionValueFromModel(fromProject)::map)
                .produce(ProjectService::getProjects)
        rsocket(DELETE_PROJECT)
                .requestMapper(longMapper.toModel)
                .validationPolicy(NOT_NULL)
                .consume(ProjectService::deleteProject)
    }


    private fun startServers() {
        startRsocketWebSocketServer()
        startHttpServer().await()
    }
}