package ru.art.information.model;

import lombok.*;

@Value
@Builder
public class GrpcServiceMethodInformation {
    private final String id;
    private final String exampleRequest;
    private final String exampleResponse;
}
