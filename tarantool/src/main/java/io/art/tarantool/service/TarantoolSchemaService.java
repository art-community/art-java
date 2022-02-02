package io.art.tarantool.service;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.tarantool.model.*;
import io.art.tarantool.storage.*;
import lombok.*;
import org.msgpack.value.Value;
import org.msgpack.value.*;
import reactor.core.publisher.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.collection.ImmutableArray.*;
import static io.art.core.collector.MapCollector.*;
import static io.art.core.extensions.ReactiveExtensions.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.Functions.*;
import static org.msgpack.value.ValueFactory.*;
import java.util.*;

@Public
@RequiredArgsConstructor
public class TarantoolSchemaService {
    private final TarantoolStorage storage;

    public void createSpace(TarantoolSpaceConfiguration configuration) {
        Map<Value, Value> options = map();
        apply(configuration.id(), value -> options.put(newString("id"), newInteger(value)));
        apply(configuration.engine(), value -> options.put(newString("engine"), newString(value.name().toLowerCase())));
        apply(configuration.fieldCount(), value -> options.put(newString("field_count"), newInteger(value)));
        apply(configuration.ifNotExists(), value -> options.put(newString("if_not_exists"), newBoolean(value)));
        apply(configuration.local(), value -> options.put(newString("is_local"), newBoolean(value)));
        apply(configuration.sync(), value -> options.put(newString("is_sync"), newBoolean(value)));
        apply(configuration.user(), value -> options.put(newString("user"), newString(value)));
        apply(configuration.temporary(), value -> options.put(newString("temporary "), newBoolean(value)));
        ArrayValue input = newArray(newString(configuration.name()), newMap(options));
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

    public void formatSpace(String space, TarantoolFormatConfiguration configuration) {
        ArrayValue input = newArray(
                newString(space),
                writeFormat(configuration)
        );
        block(storage.mutable().call(SCHEMA_FORMAT, input));
    }

    public void renameSpace(String from, String to) {
        ArrayValue input = newArray(newString(from), newString(to));
        block(storage.mutable().call(SCHEMA_RENAME_SPACE, input));
    }

    public void dropSpace(String name) {
        ArrayValue input = newArray(newString(name));
        block(storage.mutable().call(SCHEMA_DROP_SPACE, input));
    }

    public void dropIndex(String spaceName, String indexName) {
        ArrayValue input = newArray(newString(spaceName), newString(indexName));
        block(storage.mutable().call(SCHEMA_DROP_INDEX, input));
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

    public ImmutableArray<String> indicies(String space) {
        Mono<ImmutableArray<String>> spaces = storage
                .immutable()
                .call(SCHEMA_INDICIES, newArray(newString(space)))
                .map(value -> value.asArrayValue()
                        .list()
                        .stream()
                        .map(Value::asStringValue)
                        .map(StringValue::asString)
                        .collect(immutableArrayCollector()));
        return block(spaces);
    }

    private ImmutableMapValue writeFormat(TarantoolFormatConfiguration configuration) {
        return newMap(
                configuration.format()
                        .entrySet()
                        .stream()
                        .collect(mapCollector(entry -> newString(entry.getKey()), entry -> newString(entry.getValue().name().toLowerCase())))
        );
    }
}
