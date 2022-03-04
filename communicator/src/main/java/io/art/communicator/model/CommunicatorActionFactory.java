package io.art.communicator.model;

import io.art.core.model.*;
import java.util.function.*;

@FunctionalInterface
public interface CommunicatorActionFactory extends BiFunction<ConnectorIdentifier, CommunicatorActionIdentifier, Communication> {
}
