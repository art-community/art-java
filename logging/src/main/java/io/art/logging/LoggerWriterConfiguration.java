package io.art.logging;

import io.art.core.collection.*;
import io.art.core.source.*;
import lombok.Builder;
import lombok.*;
import static io.art.core.collection.ImmutableSet.*;
import static io.art.core.context.Context.*;
import static io.art.core.factory.SetFactory.*;
import static io.art.logging.LoggingLevel.*;
import static io.art.logging.LoggingWriterType.*;
import static java.time.format.DateTimeFormatter.*;
import java.nio.charset.*;
import java.time.format.*;

@Getter
@Builder(toBuilder = true)
public class LoggerWriterConfiguration {
    private final LoggingLevel level;
    private final LoggingWriterType type;
    private final ConsoleWriterConfiguration console;
    private final DateTimeFormatter dateTimeFormatter;

    @Builder.Default
    private final ImmutableSet<String> categories = emptyImmutableSet();

    @Builder.Default
    private final Boolean enabled = false;

    public static LoggerWriterConfiguration from(ConfigurationSource source) {
        LoggerWriterConfigurationBuilder builder = LoggerWriterConfiguration.builder();
        builder.level(LoggingLevel.parse(source.getString("level"), INFO));
        builder.type(LoggingWriterType.parse(source.getString("type"), CONSOLE));
        builder.console(ConsoleWriterConfiguration.builder().build());
        builder.categories(immutableSetOf(source.getStringArray("categories")));
        builder.dateTimeFormatter(ofPattern(source.getString("dateTimeFormat")));
        return builder.build();
    }

    @Getter
    @Builder
    public static class ConsoleWriterConfiguration {
        @Builder.Default
        private final Boolean colored = false;

        @Builder.Default
        private final Charset charset = context().configuration().getCharset();
    }
}
