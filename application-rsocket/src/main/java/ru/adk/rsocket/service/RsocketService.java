package ru.adk.rsocket.service;

import lombok.*;
import lombok.experimental.Accessors;
import ru.adk.entity.mapper.ValueFromModelMapper;
import ru.adk.entity.mapper.ValueToModelMapper;
import ru.adk.service.constants.RequestValidationPolicy;
import static ru.adk.rsocket.constants.RsocketModuleConstants.RsocketDataFormat;
import static ru.adk.service.constants.RequestValidationPolicy.NON_VALIDATABLE;
import java.util.Map;

@Getter
@Builder(builderMethodName = "rsocketService", buildMethodName = "serve")
public class RsocketService {
    @Singular("method")
    private final Map<String, RsocketMethod> rsocketMethods;

    @Getter
    @Setter
    @Accessors(fluent = true)
    @NoArgsConstructor(staticName = "rsocketMethod")
    public static class RsocketMethod {
        private ValueToModelMapper<?, ?> requestMapper;
        private ValueFromModelMapper<?, ?> responseMapper;
        private RequestValidationPolicy validationPolicy = NON_VALIDATABLE;
        private RsocketDataFormat overrideResponseDataFormat;
    }
}
