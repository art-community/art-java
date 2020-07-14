package io.art.rsocket.model;

import lombok.*;
import io.art.entity.Value;
import io.art.rsocket.constants.RsocketModuleConstants.*;

@Getter
@RequiredArgsConstructor(staticName = "intercepted")
@AllArgsConstructor(staticName = "intercepted")
public class RsocketValueInterceptionResult {
    private Value value;
    private final RsocketInterceptedResultAction action;
}
