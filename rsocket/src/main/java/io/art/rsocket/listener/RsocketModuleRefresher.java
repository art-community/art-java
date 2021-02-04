package io.art.rsocket.listener;

import io.art.core.managed.*;
import io.art.core.module.*;
import lombok.*;
import lombok.experimental.*;
import static io.art.core.managed.ChangesListener.*;

@Getter
@Accessors(fluent = true)
public class RsocketModuleRefresher implements ModuleRefresher {
    private final ChangesListener serverListener = listener();
}
