package ru.art.information.model;

import lombok.*;

@Value
@Builder
public class StatusResponse {
    private final boolean http;
    private final boolean grpc;
    private final boolean rsocketTcp;
    private final boolean rsocketWebSocket;
}
