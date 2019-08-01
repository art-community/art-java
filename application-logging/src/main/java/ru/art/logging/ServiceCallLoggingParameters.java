package ru.art.logging;

import lombok.Builder;
import lombok.Getter;
import java.util.Set;

@Getter
@Builder
public class ServiceCallLoggingParameters {
    private final String serviceId;
    private final String serviceMethodId;
    private final String serviceMethodCommand;
    private final String loggingEventType;
    private final Set<String> loadedServices;
}
