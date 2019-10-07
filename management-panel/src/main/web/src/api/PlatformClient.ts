import {BufferEncoders, RSocketClient} from "rsocket-core";
import RSocketWebSocketClient from "rsocket-websocket-client";
import {RSOCKET_FUNCTION, RSOCKET_OPTIONS, RSOCKET_URL} from "../constants/Constants";
import {decode, encode} from "msgpack-lite";

const connect = async () => new RSocketClient({
    setup: RSOCKET_OPTIONS,
    transport: new RSocketWebSocketClient({url: RSOCKET_URL}, BufferEncoders)
}).connect();

export const executeRequest = async (request: any, onComplete: (data: any) => void) => {
    const socket = await connect();
    const response = await socket
        .requestResponse({
            data: encode(request),
            metadata: encode(createMethodRequest(request.serviceMethodCommand.methodId, {token: "token"}))
        })
        .map(payload => decode(payload.data as number[]));
    return response.serviceExecutionException ? null : onComplete(response.responseData)
};

export const createServiceMethodRequest = (serviceId: String, methodId: String, requestData: any = null) => ({
    serviceMethodCommand: {serviceId: serviceId, methodId: methodId},
    requestData: requestData
});

export const createMethodRequest = (methodId: String, requestData: any = null) => ({
    serviceMethodCommand: {serviceId: RSOCKET_FUNCTION, methodId: methodId},
    requestData: requestData
});
