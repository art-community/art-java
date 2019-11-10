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

package ru.art.test.specification.rsocket

import reactor.core.publisher.Flux
import ru.art.core.caster.Caster
import ru.art.entity.Entity
import spock.lang.Specification
import spock.lang.Unroll

import static reactor.core.publisher.Flux.just
import static ru.art.config.extensions.activator.AgileConfigurationsActivator.useAgileConfigurations
import static ru.art.core.constants.NetworkConstants.LOCALHOST
import static ru.art.entity.Entity.merge
import static ru.art.entity.Entity.entityBuilder
import static ru.art.reactive.service.constants.ReactiveServiceModuleConstants.ReactiveMethodProcessingMode.REACTIVE
import static ru.art.rsocket.communicator.RsocketCommunicator.rsocketCommunicator
import static ru.art.rsocket.constants.RsocketModuleConstants.RsocketDataFormat.*
import static ru.art.rsocket.constants.RsocketModuleConstants.RsocketTransport.TCP
import static ru.art.rsocket.constants.RsocketModuleConstants.RsocketTransport.WEB_SOCKET
import static ru.art.rsocket.function.RsocketServiceFunction.rsocket
import static ru.art.rsocket.model.RsocketCommunicationTargetConfiguration.rsocketCommunicationTarget
import static ru.art.rsocket.module.RsocketModule.rsocketModule
import static ru.art.rsocket.server.RsocketServer.startRsocketTcpServer
import static ru.art.rsocket.server.RsocketServer.startRsocketWebSocketServer

class RsocketSpecification extends Specification {
    def functionId = "TEST_SERVICE"
    def request = entityBuilder().stringField("request", "request").build()
    def response = entityBuilder().stringField("response", "response").build()

    @Unroll
    "should communicate by rsocket (format = #format, transport = #transport, mode = fireAndForget())"() {
        setup:
        useAgileConfigurations()
        rsocket(functionId)
                .requestMapper(Caster.&cast)
                .responseMapper(Caster.&cast)
                .handle { request -> merge(request as Entity, response) }
        switch (transport) {
            case TCP:
                startRsocketTcpServer()
                break
            case WEB_SOCKET:
                startRsocketWebSocketServer()
                break
        }
        sleep(500L)
        def communicator = rsocketCommunicator(LOCALHOST, {
            switch (transport) {
                case TCP:
                    rsocketModule().serverTcpPort
                    break
                case WEB_SOCKET:
                    rsocketModule().serverWebSocketPort
                    break
            }
        }.call())
                .requestMapper(Caster.&cast)
                .responseMapper(Caster.&cast)
                .functionId(functionId)

        when:
        def response = communicator.call().blockOptional()

        then:
        response != null

        where:
        format       | transport
        PROTOBUF     | TCP
        JSON         | TCP
        MESSAGE_PACK | TCP
        PROTOBUF     | WEB_SOCKET
        JSON         | WEB_SOCKET
        MESSAGE_PACK | WEB_SOCKET
    }

    @Unroll
    "should communicate by rsocket (format = #format, transport = #transport, mode = requestResponse())"() {
        setup:
        useAgileConfigurations()
        rsocket(functionId)
                .requestMapper(Caster.&cast)
                .responseMapper(Caster.&cast)
                .handle { request -> merge(request as Entity, response) }
        switch (transport) {
            case TCP:
                startRsocketTcpServer()
                break
            case WEB_SOCKET:
                startRsocketWebSocketServer()
                break
        }
        sleep(500L)
        def communicator = rsocketCommunicator(rsocketCommunicationTarget()
                .host(LOCALHOST)
                .tcpPort({
                    switch (transport) {
                        case TCP:
                            rsocketModule().serverTcpPort
                            break
                        case WEB_SOCKET:
                            rsocketModule().serverWebSocketPort
                            break
                    }
                }.call())
                .dataFormat(format)
                .transport(transport)
                .build())
                .requestMapper(Caster.&cast)
                .responseMapper(Caster.&cast)
                .functionId(functionId)

        when:
        def response = communicator.execute().block()

        then:
        response
        (response.responseData as Entity) == this.response

        when:
        response = communicator.execute(request).block()

        then:
        response
        (response.responseData as Entity) == merge(request, this.response)

        where:
        format   | transport
        PROTOBUF     | TCP
        JSON         | TCP
        MESSAGE_PACK | TCP
        PROTOBUF     | WEB_SOCKET
        JSON         | WEB_SOCKET
        MESSAGE_PACK | WEB_SOCKET
    }

    @Unroll
    "should communicate by rsocket (format = #format, transport = #transport, mode = requestStream())"() {
        setup:
        useAgileConfigurations()
        rsocket(functionId)
                .requestMapper(Caster.&cast)
                .responseMapper(Caster.&cast)
                .responseProcessingMode(REACTIVE)
                .handle { request -> just(merge(request as Entity, response)) }
        switch (transport) {
            case TCP:
                startRsocketTcpServer()
                break
            case WEB_SOCKET:
                startRsocketWebSocketServer()
                break
        }
        sleep(500L)
        def communicator = rsocketCommunicator(rsocketCommunicationTarget()
                .host(LOCALHOST)
                .tcpPort({
                    switch (transport) {
                        case TCP:
                            rsocketModule().serverTcpPort
                            break
                        case WEB_SOCKET:
                            rsocketModule().serverWebSocketPort
                            break
                    }
                }.call())
                .dataFormat(format)
                .transport(transport)
                .build())
                .requestMapper(Caster.&cast)
                .responseMapper(Caster.&cast)
                .functionId(functionId)

        when:
        def response = communicator.stream().blockFirst()

        then:
        response
        (response.responseData as Entity) == this.response

        when:
        response = communicator.stream(request).blockFirst()

        then:
        response
        (response.responseData as Entity) == merge(request, this.response)

        where:
        format   | transport
        PROTOBUF     | TCP
        JSON         | TCP
        MESSAGE_PACK | TCP
        PROTOBUF     | WEB_SOCKET
        JSON         | WEB_SOCKET
        MESSAGE_PACK | WEB_SOCKET
    }

    @Unroll
    "should communicate by rsocket (format = #format, transport = #transport, mode = requestChannel())"() {
        setup:
        useAgileConfigurations()
        rsocket(functionId)
                .requestMapper(Caster.&cast)
                .responseMapper(Caster.&cast)
                .requestProcessingMode(REACTIVE)
                .responseProcessingMode(REACTIVE)
                .handle { request -> (request as Flux<Entity>).map { merge(it as Entity, response) } }
        switch (transport) {
            case TCP:
                startRsocketTcpServer()
                break
            case WEB_SOCKET:
                startRsocketWebSocketServer()
                break
        }

        sleep(500L)
        def communicator = rsocketCommunicator(rsocketCommunicationTarget()
                .host(LOCALHOST)
                .tcpPort({
                    switch (transport) {
                        case TCP:
                            rsocketModule().serverTcpPort
                            break
                        case WEB_SOCKET:
                            rsocketModule().serverWebSocketPort
                            break
                    }
                }.call())
                .dataFormat(format)
                .transport(transport)
                .build())
                .requestMapper(Caster.&cast)
                .responseMapper(Caster.&cast)
                .functionId(functionId)

        when:
        def response = communicator.channel(just(request)).blockFirst()

        then:
        response
        (response.responseData as Entity) == merge(request, this.response)


        where:
        format   | transport
        PROTOBUF     | TCP
        JSON         | TCP
        MESSAGE_PACK | TCP
        PROTOBUF     | WEB_SOCKET
        JSON         | WEB_SOCKET
        MESSAGE_PACK | WEB_SOCKET
    }

}