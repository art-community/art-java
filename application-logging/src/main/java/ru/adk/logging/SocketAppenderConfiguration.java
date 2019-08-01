package ru.adk.logging;

import lombok.Builder;
import lombok.Getter;
import org.apache.logging.log4j.core.Layout;

@Getter
@Builder
public class SocketAppenderConfiguration {
    private final String host;
    private final int port;
    private final Layout<?> layout;
}
