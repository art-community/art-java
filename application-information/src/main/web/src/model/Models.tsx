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