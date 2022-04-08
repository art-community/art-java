package io.art.communicator.configurator;

import io.art.communicator.*;
import io.art.core.annotation.*;
import io.art.meta.model.*;
import java.util.function.*;

@Public
public interface CommunicatorConfigurator {
    CommunicatorConfigurator communicator(Class<?> communicatorClass, UnaryOperator<CommunicatorActionConfigurator> decorator);

    <M extends MetaClass<? extends Communicator>> CommunicatorConfigurator
    communicator(Supplier<M> communicatorClass, UnaryOperator<CommunicatorActionConfigurator> decorator);

    <M extends MetaClass<? extends Communicator>> CommunicatorConfigurator
    action(Supplier<M> communicatorClass, Supplier<MetaMethod<M, ?>> actionMethod, UnaryOperator<CommunicatorActionConfigurator> decorator);
}
