package io.art.tarantool.service;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.tarantool.constants.TarantoolModuleConstants.*;
import io.art.tarantool.model.*;
import io.art.tarantool.storage.*;
import lombok.*;
import org.msgpack.value.Value;
import org.msgpack.value.*;
import reactor.core.publisher.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.collection.ImmutableArray.*;
import static io.art.core.collector.ArrayCollector.*;
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

    public TarantoolSchemaService createSpace(TarantoolSpaceConfiguration configuration) {
        Map<Value, Value> options = map();
        apply(configuration.id(), value -> options.put(newString(SpaceFields.ID), newInteger(value)));
        apply(configuration.engine(), value -> options.put(newString(SpaceFields.ENGINE), newString(value.name().toLowerCase())));
        apply(configuration.fieldCount(), value -> options.put(newString(SpaceFields.FIELD_COUNT), newInteger(value)));
        apply(configuration.ifNotExists(), value -> options.put(newString(SpaceFields.IF_NOT_EXISTS), newBoolean(value)));
        apply(configuration.local(), value -> options.put(newString(SpaceFields.IS_LOCAL), newBoolean(value)));
        apply(configuration.sync(), value -> options.put(newString(SpaceFields.IS_SYNC), newBoolean(value)));
        apply(configuration.user(), value -> options.put(newString(SpaceFields.USER), newString(value)));
        apply(configuration.temporary(), value -> options.put(newString(SpaceFields.TEMPORARY), newBoolean(value)));
        apply(configuration.format(), value -> options.put(newString(SpaceFields.FORMAT), writeFormat(value)));
        ArrayValue input = newArray(newString(configuration.name()), newMap(options));
        block(storage.mutable().call(SCHEMA_CREATE_SPACE, input));
        return this;
    }

    public TarantoolSchemaService createIndex(TarantoolIndexConfiguration<?, ?> configuration) {
        Map<Value, Value> options = map();
        apply(configuration.id(), value -> options.put(newString(IndexFields.ID), newInteger(value)));
        apply(configuration.sequence(), value -> options.put(newString(IndexFields.SEQUENCE), newString(value)));
        apply(configuration.func(), value -> options.put(newString(IndexFields.FUNC), newString(value)));
        apply(configuration.type(), value -> options.put(newString(IndexFields.TYPE), newString(value.name().toLowerCase())));
        apply(configuration.unique(), value -> options.put(newString(IndexFields.UNIQUE), newBoolean(value)));
        apply(configuration.treeConfiguration(), value -> apply(value.hint(), treeValue -> options.put(newString(IndexFields.HINT), newBoolean(treeValue))));
        apply(configuration.rtreeConfiguration(), value -> writeRtreeConfiguration(options, value));
        apply(configuration.vinylConfiguration(), value -> writeVinylConfiguration(options, value));
        apply(configuration.ifNotExists(), value -> options.put(newString(IndexFields.IF_NOT_EXISTS), newBoolean(value)));
        options.put(newString(IndexFields.PARTS), newArray(configuration.parts().stream().map(this::writeIndexPart).collect(listCollector())));
        ArrayValue input = newArray(newString(configuration.spaceName()), newString(configuration.indexName()), newMap(options));
        block(storage.mutable().call(SCHEMA_CREATE_INDEX, input));
        return this;
    }

    public TarantoolSchemaService formatSpace(String space, TarantoolFormatConfiguration configuration) {
        ArrayValue input = newArray(newString(space), writeFormat(configuration));
        block(storage.mutable().call(SCHEMA_FORMAT, input));
        return this;
    }

    public TarantoolSchemaService renameSpace(String from, String to) {
        ArrayValue input = newArray(newString(from), newString(to));
        block(storage.mutable().call(SCHEMA_RENAME_SPACE, input));
        return this;
    }

    public TarantoolSchemaService dropSpace(String name) {
        ArrayValue input = newArray(newString(name));
        block(storage.mutable().call(SCHEMA_DROP_SPACE, input));
        return this;
    }

    public TarantoolSchemaService dropIndex(String spaceName, String indexName) {
        ArrayValue input = newArray(newString(spaceName), newString(indexName));
        block(storage.mutable().call(SCHEMA_DROP_INDEX, input));
        return this;
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

    public boolean hasSpace(String space) {
        return spaces().stream().anyMatch(space::equals);
    }

    public boolean hasIndex(String space, String index) {
        return indicies(space).stream().anyMatch(index::equals);
    }

    public boolean hasIndex(String index) {
        return spaces().stream().flatMap(space -> indicies(space).stream()).anyMatch(index::equals);
    }

    private ImmutableMapValue writeFormat(TarantoolFormatConfiguration configuration) {
        return newMap(
                configuration.format()
                        .entrySet()
                        .stream()
                        .collect(mapCollector(entry -> newString(entry.getKey()), entry -> newString(entry.getValue().name().toLowerCase())))
        );
    }

    private ImmutableMapValue writeIndexPart(TarantoolIndexPartConfiguration<?, ?> configuration) {
        Map<Value, Value> map = map();
        map.put(newString(IndexPartFields.FIELD), newInteger(configuration.field()));
        map.put(newString(IndexPartFields.TYPE), newString(configuration.type().name().toLowerCase()));
        apply(configuration.nullable(), value -> map.put(newString(IndexPartFields.IS_NULLABLE), newBoolean(value)));
        apply(configuration.path(), value -> map.put(newString(IndexPartFields.PATH), newString(value)));
        return newMap(map);
    }

    private void writeRtreeConfiguration(Map<Value, Value> options, TarantoolIndexConfiguration.TarantoolRtreeIndexConfiguration value) {
        apply(value.dimension(), rtreeValue -> options.put(newString(IndexFields.DIMENSION), newInteger(rtreeValue)));
        apply(value.distance(), rtreeValue -> options.put(newString(IndexFields.DISTANCE), newString(rtreeValue)));
    }

    private void writeVinylConfiguration(Map<Value, Value> options, TarantoolIndexConfiguration.TarantoolVinylIndexConfiguration value) {
        apply(value.bloomFrp(), vinylValue -> options.put(newString(IndexFields.BLOOM_FPR), newInteger(vinylValue)));
        apply(value.pageSize(), vinylValue -> options.put(newString(IndexFields.PAGE_SIZE), newInteger(vinylValue)));
        apply(value.rangeSize(), vinylValue -> options.put(newString(IndexFields.RANGE_SIZE), newInteger(vinylValue)));
        apply(value.runCountPerLevel(), vinylValue -> options.put(newString(IndexFields.RUN_COUNT_PER_LEVEL), newInteger(vinylValue)));
        apply(value.runSizeRatio(), vinylValue -> options.put(newString(IndexFields.RUN_SIZE_RATIO), newInteger(vinylValue)));
    }

}
