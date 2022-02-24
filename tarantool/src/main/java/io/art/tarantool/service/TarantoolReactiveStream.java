package io.art.tarantool.service;

import io.art.core.model.*;
import io.art.meta.model.*;
import io.art.storage.*;
import io.art.storage.SpaceStream.*;
import io.art.tarantool.descriptor.*;
import io.art.tarantool.storage.*;
import lombok.*;
import org.msgpack.value.Value;
import reactor.core.publisher.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.Functions.*;
import static org.msgpack.value.ValueFactory.*;
import static reactor.core.publisher.Flux.*;
import java.util.*;


@AllArgsConstructor
public class TarantoolReactiveStream<Type, Meta extends MetaClass<Type>> extends ReactiveSpaceStream<TarantoolReactiveStream<Type, Meta>, Type, Meta> {
    private final TarantoolStorage storage;
    private final TarantoolModelReader reader;
    private final MetaType<Type> spaceMeta;

    @Override
    public Flux<Type> collect() {
        List<Value> serialized = linkedList();
        for (Pair<StreamOperation, Object> operator : operators) {
            switch (operator.getFirst()) {
                case LIMIT:

                    break;
                case OFFSET:
                    break;
                case DISTINCT:
                    break;
                case SORT:
                    break;
                case FILTER:
                    break;
            }
        }

        return parseSpaceFlux(storage.immutable().call(SPACE_FIND, newArray(serialized)));
    }

    private Flux<Type> parseSpaceFlux(Mono<Value> value) {
        return value.flatMapMany(elements -> fromStream(elements.asArrayValue()
                .list()
                .stream()
                .map(element -> reader.read(spaceMeta, element))));
    }
}
