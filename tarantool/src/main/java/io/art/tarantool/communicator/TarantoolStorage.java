package io.art.tarantool.communicator;

import io.art.core.annotation.*;
import io.art.storage.*;
import static io.art.core.caster.Caster.*;
import static io.art.tarantool.communicator.TarantoolCommunication.*;
import java.util.function.*;

@Public
public interface TarantoolStorage<C extends TarantoolStorage<C>> extends Storage {
    default C decorate(UnaryOperator<TarantoolCommunicationDecorator> decorator) {
        decorateTarantoolFunctionCommunication(decorator);
        return cast(this);
    }

    default C immutable() {
        return decorate(TarantoolCommunicationDecorator::immutable);
    }

    default C mutable() {
        return decorate(TarantoolCommunicationDecorator::mutable);
    }

    default C channel() {
        return decorate(TarantoolCommunicationDecorator::channel);
    }
}
