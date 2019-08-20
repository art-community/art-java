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

package ru.art.test.specification.http

import ru.art.core.caster.Caster
import ru.art.entity.Entity
import spock.lang.Specification

import static java.util.Objects.isNull
import static ru.art.config.extensions.activator.AgileConfigurationsActivator.useAgileConfigurations
import static ru.art.core.constants.NetworkConstants.LOCALHOST
import static ru.art.entity.Entity.concat
import static ru.art.entity.Entity.entityBuilder
import static ru.art.http.client.communicator.HttpCommunicator.httpCommunicator
import static ru.art.http.server.HttpServer.httpServerInSeparatedThread
import static ru.art.http.server.function.HttpServiceFunction.httpPost
import static ru.art.http.server.module.HttpServerModule.httpServerModule

class HttpSpecification extends Specification {
    def listeningPath = "/serve"
    def request = entityBuilder().stringField("request", "request").build()
    def response = entityBuilder().stringField("response", "response").build()

    def "should communicate by HTTP with request body"() {
        setup:
        useAgileConfigurations()
        httpPost(listeningPath)
                .fromBody()
                .requestMapper(Caster.&cast)
                .responseMapper(Caster.&cast)
                .handle { request -> concat(request as Entity, response) }
        httpServerInSeparatedThread()
        sleep(500L)

        when:
        def communicator = httpCommunicator("http://$LOCALHOST:${httpServerModule().getPort()}$listeningPath")
                .post()
                .requestMapper(Caster.&cast)
                .responseMapper(Caster.&cast)
        def response = communicator.execute()

        then:
        response
        (response.get() as Entity) == this.response

        when:
        communicator = httpCommunicator("http://$LOCALHOST:${httpServerModule().getPort()}$listeningPath")
                .post()
                .requestMapper(Caster.&cast)
                .responseMapper(Caster.&cast)
        response = communicator.execute(request)

        then:
        response
        (response.get() as Entity) == concat(request, this.response)

        when:
        communicator = httpCommunicator("http://$LOCALHOST:${httpServerModule().getPort()}$listeningPath")
                .post()
                .requestMapper(Caster.&cast)
                .responseMapper(Caster.&cast)
        def asyncResponse = null
        communicator.asynchronous()
                .exceptionHandler { println it }
                .completionHandler { req, resp -> asyncResponse = resp }
                .executeAsynchronous()
        while (isNull(asyncResponse)) {

        }

        then:
        asyncResponse.get() == this.response

        when:
        communicator = httpCommunicator("http://$LOCALHOST:${httpServerModule().getPort()}$listeningPath")
                .post()
                .requestMapper(Caster.&cast)
                .responseMapper(Caster.&cast)
        def asyncResponseWithReq = null
        communicator.asynchronous()
                .exceptionHandler { println it }
                .completionHandler { req, resp -> asyncResponseWithReq = resp }
                .executeAsynchronous(request)
        while (isNull(asyncResponseWithReq)) {

        }

        then:
        (asyncResponseWithReq.get() as Entity) == concat(request, this.response)
    }
}