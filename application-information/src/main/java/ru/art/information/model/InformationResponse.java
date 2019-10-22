package ru.art.information.model;

import lombok.*;

@Value
@Builder
public class InformationResponse {
    private final GrpcInformation grpcInformation;
    private final HttpInformation httpInformation;
    private final RsocketInformation rsocketInformation;
}
