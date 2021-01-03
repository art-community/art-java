package io.art.communicator.proxy;

import io.art.communicator.implementation.*;
import io.art.core.collection.*;
import static io.art.communicator.constants.CommunicatorModuleConstants.*;

public interface CommunicatorProxy<T extends CommunicatorImplementation> {
    ImmutableArray<T> getImplementations();
    CommunicationProtocol getProtocol();
}
