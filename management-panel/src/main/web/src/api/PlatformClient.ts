import {BufferEncoders, RSocketClient} from "rsocket-core";
import RSocketWebSocketClient from "rsocket-websocket-client";
import {EMPTY_RESPONSE, RSOCKET_FUNCTION, RSOCKET_OPTIONS, RSOCKET_URL, TOKEN_COOKIE} from "../constants/Constants";
import {decode, encode} from "msgpack-lite";
import Cookies from "js-cookie";
import {ServiceResponse} from "../model/Models";

const connect = async () => new RSocketClient({
    setup: RSOCKET_OPTIONS,
    transport: new RSocketWebSocketClient({url: RSOCKET_URL}, BufferEncoders)
}).connect();

export const requestResponse = (request: any, onComplete: (result: any) => void = () => {}, onError: (error: any) => void = () => {}) => {
    connect()
        .then(rsocket => rsocket
            .requestResponse({
                data: encode(request),
                metadata: encode(createMethodRequest(request.serviceMethodCommand.methodId, Cookies.get(TOKEN_COOKIE)))
            })
            .map(payload => payload.data ? decode(payload.data as number[]) : null))
        .then((response: ServiceResponse) => {
            if (!response) {
                console.error(Error(EMPTY_RESPONSE));
                onError(Error(EMPTY_RESPONSE))
            }
            if (response.serviceExecutionException) {
                console.error(response.serviceExecutionException);
                onError(response.serviceExecutionException)
            }
            onComplete(response.responseData);
        })
};

export const requestStream = (request: any, onNext: (result: any) => void = () => {}, onError: (error: any) => void = () => {}) => {
    connect()
        .then(rsocket => rsocket.requestStream({
            data: encode(request),
            metadata: encode(createMethodRequest(request.serviceMethodCommand.methodId, Cookies.get(TOKEN_COOKIE)))
        }))
        .then(flux => flux.map(payload => payload.data ? decode(payload.data as number[]) : null)
            .subscribe(response => {
                if (!response) {
                    console.error(Error(EMPTY_RESPONSE));
                    onError(Error(EMPTY_RESPONSE))
                }
                if (response.serviceExecutionException) {
                    console.error(response.serviceExecutionException);
                    onError(response.serviceExecutionException)
                }
                onNext(response.responseData)
            }));
};

export const fireAndForget = (request: any) => {
    connect().then(socket => socket.fireAndForget({
        data: encode(request),
        metadata: encode(createMethodRequest(request.serviceMethodCommand.methodId, Cookies.get(TOKEN_COOKIE)))
    }))
};

export const createServiceMethodRequest = (serviceId: string, methodId: string, requestData: any = null) => ({
    serviceMethodCommand: {serviceId: serviceId, methodId: methodId},
    requestData: requestData
});

export const createMethodRequest = (methodId: string, requestData: any = null) => ({
    serviceMethodCommand: {serviceId: RSOCKET_FUNCTION, methodId: methodId},
    requestData: requestData
});
