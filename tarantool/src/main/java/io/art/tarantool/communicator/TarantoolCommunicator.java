package io.art.tarantool.communicator;

import io.art.communicator.*;
import io.art.core.annotation.*;
import static io.art.core.caster.Caster.*;
import static io.art.tarantool.communicator.TarantoolFunctionCommunication.*;
import java.util.function.*;

@Public
public interface TarantoolCommunicator<C extends TarantoolCommunicator<C>> extends Communicator {
    default C decorate(UnaryOperator<TarantoolCommunicationDecorator> decorator) {
        decorateTarantoolCommunication(decorator);
        return cast(this);
    }

    default C immutable() {
        return decorate(TarantoolCommunicationDecorator::immutable);
    }

    default C mutable() {
        return decorate(TarantoolCommunicationDecorator::mutable);
    }
}
