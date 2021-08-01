package io.art.communicator.proxy;

import io.art.communicator.action.*;
import io.art.core.collection.*;
import io.art.core.model.*;
import lombok.*;

@Getter
@RequiredArgsConstructor
public class CommunicatorProxy<T> {
    private final T proxy;
    private final ImmutableMap<CommunicatorActionIdentifier, CommunicatorAction> actions;
}
