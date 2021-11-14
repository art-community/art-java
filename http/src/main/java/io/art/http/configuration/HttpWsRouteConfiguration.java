package io.art.http.configuration;

import lombok.*;

@Getter
@Builder(toBuilder = true)
public class HttpWsRouteConfiguration {
    private final int aggregateFrames;
}

