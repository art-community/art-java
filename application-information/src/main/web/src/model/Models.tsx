export interface GrpcInformation {
    url: string;
    services: { [index: string]: GrpcServiceInformation };
}

export interface GrpcServiceInformation {
    id: string;
    methods: { [index: string]: GrpcServiceMethodInformation };
}

export interface GrpcServiceMethodInformation {
    id: string;
    exampleRequest: string;
    exampleResponse: string;
}

export interface HttpInformation {
    services: { [index: string]: HttpServiceInformation };
}

export interface HttpServiceInformation {
    id: string;
    methods: { [index: string]: HttpServiceMethodInformation };
}

export interface HttpServiceMethodInformation {
    method: string;
    id: string;
    url: string;
    exampleRequest: string;
    exampleResponse: string;
}

export interface InformationResponse {
    grpcInformation: GrpcInformation;
    httpInformation: HttpInformation;
    rsocketInformation: RsocketInformation;
    statusResponse: Status;
}

export interface Status {
    http: boolean;
    grpc: boolean;
    rsocketTcp: boolean;
    rsocketWebSocket: boolean;
}

export const FALSE_STATUS = {
    http: false,
    grpc: false,
    rsocketWebSocket: false,
    rsocketTcp: false
};

export interface RsocketInformation {
    webSocketUrl: string;
    tcpUrl: string;
    services: { [index: string]: RsocketServiceInformation };
}

export interface RsocketServiceInformation {
    id: string;
    methods: { [index: string]: RsocketServiceMethodInformation };
}

export interface RsocketServiceMethodInformation {
    id: string;
    exampleRequest: string;
    exampleResponse: string;
}