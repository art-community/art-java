package io.art.tarantool.space;

import io.art.core.annotation.*;
import io.art.storage.*;
import static io.art.core.caster.Caster.*;
import static io.art.tarantool.space.TarantoolSpaceCommunication.*;
import java.util.function.*;

@Public
public interface TarantoolSpace<C extends TarantoolSpace<C>> extends Space {
    default C decorate(UnaryOperator<TarantoolSpaceDecorator> decorator) {
        decorateTarantoolSpace(decorator);
        return cast(this);
    }

    default C immutable() {
        return decorate(TarantoolSpaceDecorator::immutable);
    }

    default C mutable() {
        return decorate(TarantoolSpaceDecorator::mutable);
    }
}
