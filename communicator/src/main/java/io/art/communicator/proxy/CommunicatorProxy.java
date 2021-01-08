package io.art.communicator.proxy;

import io.art.communicator.specification.*;
import io.art.core.collection.*;
import static io.art.communicator.constants.CommunicatorModuleConstants.*;

public interface CommunicatorProxy {
    ImmutableArray<CommunicatorSpecification> getSpecifications();

    CommunicationProtocol getProtocol();
}
