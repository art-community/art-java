package io.art.tarantool.service;

import io.art.core.collection.*;
import io.art.meta.model.*;
import io.art.storage.*;
import io.art.tarantool.descriptor.*;
import io.art.tarantool.storage.*;
import lombok.*;
import org.msgpack.value.Value;
import reactor.core.publisher.*;
import static io.art.core.collector.ArrayCollector.*;
import static io.art.meta.Meta.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.Functions.*;
import static java.util.Arrays.*;
import java.util.*;
import java.util.function.*;

@RequiredArgsConstructor
public class TarantoolSpaceService<KeyType, ValueType extends Space> {
    private final MetaClass<ValueType> space;
    private final Supplier<TarantoolStorage> storage;
    private final TarantoolModelWriter writer;
    private final TarantoolModelReader reader;

    public TarantoolDefaultResponse findFirst(KeyType key) {
        Mono<Value> output = storage.get().immutable().call(SPACE_FIND_FIRST, writer.write(definition(key.getClass()), key));
        return new TarantoolDefaultResponse(output, space);
    }

    @SafeVarargs
    public final TarantoolDefaultResponse findAll(KeyType... keys) {
        return findAll(asList(keys));
    }

    public TarantoolDefaultResponse findAll(Collection<KeyType> keys) {
        List<Value> input = keys.stream().map(key -> writer.write(definition(key.getClass()), key)).collect(listCollector());
        Mono<Value> output = storage.get().immutable().call(SPACE_FIND_ALL, input);
        return new TarantoolDefaultResponse(output, space);
    }

    public TarantoolDefaultResponse findAll(ImmutableCollection<KeyType> keys) {
        List<Value> input = keys.stream().map(key -> writer.write(definition(key.getClass()), key)).collect(listCollector());
        Mono<Value> output = storage.get().immutable().call(SPACE_FIND_ALL, input);
        return new TarantoolDefaultResponse(output, space);
    }
}
