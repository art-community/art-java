package io.art.tarantool.service;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.tarantool.descriptor.*;
import io.art.tarantool.model.*;
import io.art.tarantool.storage.*;
import org.msgpack.value.*;
import reactor.core.publisher.*;
import static io.art.core.collection.ImmutableArray.*;
import static io.art.core.extensions.ReactiveExtensions.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.Functions.*;
import static io.art.tarantool.module.TarantoolModule.*;
import static org.msgpack.value.ValueFactory.*;

@Public
public class TarantoolSchemaService {
    private final TarantoolStorage storage;
    private final TarantoolModelWriter writer;
    private final TarantoolModelReader reader;

    public TarantoolSchemaService(TarantoolStorage storage) {
        this.storage = storage;
        writer = tarantoolModule().configuration().getWriter();
        reader = tarantoolModule().configuration().getReader();
    }


    public void createSpace(TarantoolSpaceConfiguration configuration) {
        ArrayValue input = newArray(newString(configuration.name()),
                newMap(
                        newString("if_not_exists"), newBoolean(configuration.ifNotExists())
                )
        );
        block(storage.mutable().call(SCHEMA_CREATE_SPACE, input));
    }

    public void createIndex(TarantoolIndexConfiguration configuration) {
        ArrayValue input = newArray(newString(configuration.spaceName()),
                newString(configuration.indexName()),
                newMap(
                        newString("unique"), newBoolean(true),
                        newString("if_not_exists"), newBoolean(configuration.ifNotExists()),
                        newString("parts"), newArray(
                                newMap(newString("field"), newInteger(1), newString("type"), newString("unsigned"))
                        )
                )
        );
        block(storage.mutable().call(SCHEMA_CREATE_INDEX, input));
    }

    public ImmutableArray<String> spaces() {
        Mono<ImmutableArray<String>> spaces = storage
                .immutable()
                .call(SCHEMA_SPACES)
                .map(value -> value.asArrayValue()
                        .list()
                        .stream()
                        .map(Value::asStringValue)
                        .map(StringValue::asString)
                        .collect(immutableArrayCollector()));
        return block(spaces);
    }
}
