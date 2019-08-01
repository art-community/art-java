package ru.adk.logging;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProtocolCallLoggingParameters {
    private final String protocol;
    private final String traceId;
    private final String requestId;
    private final String environment;
    private final String profile;
}
