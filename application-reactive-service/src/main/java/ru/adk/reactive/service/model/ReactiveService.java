package ru.adk.reactive.service.model;

import lombok.*;
import lombok.experimental.Accessors;
import static ru.adk.reactive.service.constants.ReactiveServiceModuleConstants.ReactiveMethodProcessingMode;
import static ru.adk.reactive.service.constants.ReactiveServiceModuleConstants.ReactiveMethodProcessingMode.STRAIGHT;
import java.util.Map;

@Getter
@Builder(builderMethodName = "reactiveService", buildMethodName = "serve")
public class ReactiveService {
    @Singular("method")
    private final Map<String, ReactiveMethod> methods;

    @Getter
    @Setter
    @Accessors(fluent = true)
    @NoArgsConstructor(staticName = "reactiveMethod")
    public static class ReactiveMethod {
        private ReactiveMethodProcessingMode requestProcessingMode = STRAIGHT;
        private ReactiveMethodProcessingMode responseProcessingMode = STRAIGHT;
    }
}