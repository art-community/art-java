package io.art.logging;

import io.art.core.exception.*;
import lombok.experimental.*;

@UtilityClass
public class LoggerWriterFactory {
    public static LoggerWriter loggerWriter(String name, LoggerWriterConfiguration configuration) {
        switch (configuration.getType()) {
            case CONSOLE:
                return new ConsoleWriter(name, configuration);
        }
        throw new ImpossibleSituationException();
    }
}
