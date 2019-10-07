package ru.art.platform.interceptor

import io.rsocket.*
import io.rsocket.util.*
import org.reactivestreams.*
import reactor.core.publisher.*
import ru.art.entity.Value.*
import ru.art.platform.constants.ServiceConstants.REGISTER_USER
import ru.art.platform.service.UserService.checkToken
import ru.art.rsocket.module.RsocketModule.*
import ru.art.rsocket.reader.RsocketPayloadReader.*
import ru.art.rsocket.selector.RsocketDataFormatMimeTypeConverter.*

class AuthorizationRSocket(rsocket: RSocket) : RSocketProxy(rsocket) {
    override fun requestStream(payload: Payload): Flux<Payload> = asEntity(readPayloadMetaData(payload, fromMimeType(rsocketModuleState().currentRocketState().dataMimeType)))
            .takeIf { entity -> entity.getString("functionId") != REGISTER_USER }
            ?.let { entity -> checkToken(entity.getString("token")).let { super.requestStream(payload) } }
            ?: super.requestStream(payload)

    override fun requestResponse(payload: Payload): Mono<Payload> = asEntity(readPayloadMetaData(payload, fromMimeType(rsocketModuleState().currentRocketState().dataMimeType)))
            .takeIf { entity -> entity.getString("functionId")?.let { functionId -> functionId != REGISTER_USER } == true }
            ?.let { entity -> checkToken(entity.getString("token")).let { super.requestResponse(payload) } }
            ?: super.requestResponse(payload)

    override fun fireAndForget(payload: Payload): Mono<Void> = asEntity(readPayloadMetaData(payload, fromMimeType(rsocketModuleState().currentRocketState().dataMimeType)))
            .takeIf { entity -> entity.getString("functionId") != REGISTER_USER }
            ?.let { entity -> checkToken(entity.getString("token")).let { super.fireAndForget(payload) } }
            ?: super.fireAndForget(payload)

    override fun requestChannel(payloads: Publisher<Payload>): Flux<Payload> = super.requestChannel(payloads.toFlux()
            .doOnNext { payload -> checkToken(asPrimitive(readPayloadMetaData(payload, fromMimeType(rsocketModuleState().currentRocketState().dataMimeType))).string) })
}