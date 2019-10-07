import {BufferEncoders, RSocketClient} from "rsocket-core";
import RSocketWebSocketClient from "rsocket-websocket-client";
import {RSOCKET_FUNCTION, RSOCKET_OPTIONS, RSOCKET_URL, TOKEN_COOKIE} from "../constants/Constants";
import {decode, encode} from "msgpack-lite";
// @ts-ignore
import Cookies from "js-cookie";

const connect = async () => new RSocketClient({
    setup: RSOCKET_OPTIONS,
    transport: new RSocketWebSocketClient({url: RSOCKET_URL}, BufferEncoders)
}).connect();

export const executeRequest = async (request: any, onComplete: (data: any) => void, onError: (exception: any) => void = () => {}) => {
    const [token] = Cookies.get(TOKEN_COOKIE);
    const socket = await connect();
    const response = await socket
        .requestResponse({
            data: encode(request),
            metadata: encode(createMethodRequest(request.serviceMethodCommand.methodId, token))
        })
        .map(payload => decode(payload.data as number[]));
    if (response.serviceExecutionException) {
        console.error(response.serviceExecutionException);
        onError(response.serviceExecutionException);
        return
    }
    onComplete(response.responseData)
};

export const createServiceMethodRequest = (serviceId: String, methodId: String, requestData: any = null) => ({
    serviceMethodCommand: {serviceId: serviceId, methodId: methodId},
    requestData: requestData
});

export const createMethodRequest = (methodId: String, requestData: any = {}) => ({
    serviceMethodCommand: {serviceId: RSOCKET_FUNCTION, methodId: methodId},
    requestData: requestData
});
