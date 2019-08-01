package ru.art.logging;

import lombok.Builder;
import lombok.Getter;
import org.apache.logging.log4j.core.Layout;

@Getter
@Builder
public class ConsoleAppenderConfiguration {
    private final Layout<?> patternLayout;
}
