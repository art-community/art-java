interface GrpcInformation {
    url: string;
    services: { [index: string]: GrpcServiceInformation };
}

interface GrpcServiceInformation {
    id: string;
    methods: { [index: string]: GrpcServiceMethodInformation };
}

interface GrpcServiceMethodInformation {
    id: string;
    exampleRequest: string;
    exampleResponse: string;
}

interface HttpInformation {
    services: { [index: string]: HttpServiceInformation };
}

interface HttpServiceInformation {
    id: string;
    methods: { [index: string]: HttpServiceMethodInformation };
}

interface HttpServiceMethodInformation {
    method: string;
    id: string;
    url: string;
    exampleRequest: string;
    exampleResponse: string;
}

interface InformationResponse {
    grpcInformation: GrpcInformation;
    httpInformation: HttpInformation;
    rsocketInformation: RsocketInformation;
}

interface RsocketInformation {
    webSocketUrl: string;
    tcpUrl: string;
    services: { [index: string]: RsocketServiceInformation };
}

interface RsocketServiceInformation {
    id: string;
    methods: { [index: string]: RsocketServiceMethodInformation };
}

interface RsocketServiceMethodInformation {
    id: string;
    exampleRequest: string;
    exampleResponse: string;
}