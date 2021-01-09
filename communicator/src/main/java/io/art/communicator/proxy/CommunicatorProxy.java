package io.art.communicator.proxy;

import io.art.communicator.action.*;
import io.art.core.collection.*;
import static io.art.communicator.constants.CommunicatorModuleConstants.*;

public interface CommunicatorProxy {
    ImmutableMap<String, CommunicatorAction> getActions();

    CommunicatorProtocol getProtocol();
}
