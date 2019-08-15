/*
 * ART Java
 *
 * Copyright 2019 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.art.test.specification.grpc

import ru.art.core.caster.Caster
import ru.art.entity.Entity
import spock.lang.Specification

import static java.util.Objects.isNull
import static ru.art.config.extensions.activator.AgileConfigurationsActivator.useAgileConfigurations
import static ru.art.core.constants.NetworkConstants.LOCALHOST
import static ru.art.entity.Entity.concat
import static ru.art.entity.Entity.entityBuilder
import static ru.art.grpc.client.communicator.GrpcCommunicator.grpcCommunicator
import static ru.art.grpc.server.GrpcServer.grpcServerInSeparatedThread
import static ru.art.grpc.server.constants.GrpcServerModuleConstants.EXECUTE_GRPC_FUNCTION
import static ru.art.grpc.server.function.GrpcServiceFunction.grpc
import static ru.art.grpc.server.module.GrpcServerModule.grpcServerModule

class GrpcSpecification extends Specification {
    def serviceId = "TEST_SERVICE"
    def request = entityBuilder().stringField("request", "request").build()
    def response = entityBuilder().stringField("response", "response").build()

    def "should communicate by GRPC"() {

        setup:
        useAgileConfigurations()
        grpc(serviceId)
                .requestMapper(Caster.&cast)
                .responseMapper(Caster.&cast)
                .handle { request -> concat(request as Entity, response) }
        grpcServerInSeparatedThread()
        sleep(500L)

        when:
        def communicator = grpcCommunicator(LOCALHOST, grpcServerModule().getPort(), grpcServerModule().getPath())
                .requestMapper(Caster.&cast)
                .responseMapper(Caster.&cast)
                .serviceId(serviceId)
                .methodId(EXECUTE_GRPC_FUNCTION)
        def response = communicator.execute()

        then:
        response != null
        (response.responseData as Entity) == this.response

        when:
         communicator = grpcCommunicator(LOCALHOST, grpcServerModule().getPort(), grpcServerModule().getPath())
                .requestMapper(Caster.&cast)
                .responseMapper(Caster.&cast)
                .serviceId(serviceId)
                .methodId(EXECUTE_GRPC_FUNCTION)
        response = communicator.execute(request)

        then:
        response != null
        (response.responseData as Entity) == concat(request, this.response)

        when:
        communicator = grpcCommunicator(LOCALHOST, grpcServerModule().getPort(), grpcServerModule().getPath())
                .requestMapper(Caster.&cast)
                .responseMapper(Caster.&cast)
                .serviceId(serviceId)
                .methodId(EXECUTE_GRPC_FUNCTION)
        def asyncResponse = null
        communicator.asynchronous()
                .completionHandler { req, resp -> asyncResponse = resp.responseData }
                .executeAsynchronous()
        while (isNull(asyncResponse)) {

        }

        then:
        asyncResponse == this.response

        when:
        communicator = grpcCommunicator(LOCALHOST, grpcServerModule().getPort(), grpcServerModule().getPath())
                .requestMapper(Caster.&cast)
                .responseMapper(Caster.&cast)
                .serviceId(serviceId)
                .methodId(EXECUTE_GRPC_FUNCTION)
        def asyncResponseWithReq = null
        communicator.asynchronous()
                .completionHandler { req, resp -> asyncResponseWithReq = resp.responseData }
                .executeAsynchronous(request)
        while (isNull(asyncResponseWithReq)) {

        }

        then:
        (asyncResponseWithReq as Entity) == concat(request, this.response)
    }
}