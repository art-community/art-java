import {BufferEncoders, RSocketClient} from "rsocket-core";
import RSocketWebSocketClient from "rsocket-websocket-client";
import {EMPTY_RESPONSE, RSOCKET_FUNCTION, RSOCKET_OPTIONS, RSOCKET_URL, TOKEN_COOKIE} from "../constants/Constants";
import {decode, encode} from "msgpack-lite";
// @ts-ignore
import Cookies from "js-cookie";

const connect = async () => new RSocketClient({
    setup: RSOCKET_OPTIONS,
    transport: new RSocketWebSocketClient({url: RSOCKET_URL}, BufferEncoders)
}).connect();

export const requestResponse = async (request: any) => {
    const socket = await connect();
    const response = await socket
        .requestResponse({
            data: encode(request),
            metadata: encode(createMethodRequest(request.serviceMethodCommand.methodId, Cookies.get(TOKEN_COOKIE)))
        })
        .map(payload => payload.data != null ? decode(payload.data as number[]) : null);
    if (!response) {
        console.error(Error(EMPTY_RESPONSE));
        throw Error(EMPTY_RESPONSE)
    }
    if (response.serviceExecutionException) {
        console.error(response.serviceExecutionException);
        throw response.serviceExecutionException
    }
    return response.responseData
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
