package io.art.communicator.model;

import io.art.communicator.action.*;
import io.art.meta.model.*;
import java.util.function.*;

@FunctionalInterface
public interface CommunicatorActionProvider extends Function<MetaMethod<MetaClass<?>, ?>, CommunicatorAction> {
}
