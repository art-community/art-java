package ru.art.rsocket.resume;

import io.rsocket.resume.*;
import lombok.*;
import org.apache.logging.log4j.*;
import org.reactivestreams.*;
import static lombok.AccessLevel.*;
import static reactor.core.publisher.Flux.*;
import static ru.art.logging.LoggingModule.*;
import static ru.art.rsocket.constants.RsocketModuleConstants.LoggingMessages.*;

@AllArgsConstructor
public class TracingResumeStrategy implements ResumeStrategy {
    private final ResumeStrategy resumeStrategy;
    @Getter(lazy = true, value = PRIVATE)
    private final static Logger logger = loggingModule().getLogger(TracingResumeStrategy.class);

    @Override
    public Publisher<?> apply(ClientResume clientResume, Throwable throwable) {
        return from(resumeStrategy.apply(clientResume, throwable)).doOnNext(value -> getLogger().warn(RSOCKET_RESUMING));
    }
}
