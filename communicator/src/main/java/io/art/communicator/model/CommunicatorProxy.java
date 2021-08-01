package io.art.communicator.model;

import io.art.communicator.action.*;
import io.art.core.collection.*;
import io.art.core.model.*;
import lombok.*;

@Getter
@RequiredArgsConstructor
public class CommunicatorProxy<T extends Communicator> {
    private final T proxy;
    private final ImmutableMap<CommunicatorActionIdentifier, CommunicatorAction> actions;
}
