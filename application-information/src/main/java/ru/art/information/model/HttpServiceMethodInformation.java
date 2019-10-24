package ru.art.information.model;

import lombok.*;

@Value
@Builder
public class HttpServiceMethodInformation {
    private final String method;
    private final String id;
    private final String url;
    private final String exampleRequest;
    private final String exampleResponse;
}
