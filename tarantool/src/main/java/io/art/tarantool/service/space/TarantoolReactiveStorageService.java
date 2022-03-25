package io.art.tarantool.service.space;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.meta.model.*;
import io.art.storage.index.*;
import io.art.storage.service.*;
import io.art.storage.sharder.*;
import io.art.storage.updater.*;
import io.art.tarantool.connector.*;
import io.art.tarantool.descriptor.*;
import io.art.tarantool.serializer.*;
import io.art.tarantool.service.index.*;
import io.art.tarantool.stream.*;
import lombok.*;
import org.msgpack.value.Value;
import org.msgpack.value.*;
import reactor.core.publisher.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.collector.ArrayCollector.*;
import static io.art.core.model.Tuple.*;
import static io.art.core.normalizer.ClassIdentifierNormalizer.*;
import static io.art.meta.registry.BuiltinMetaTypes.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.Functions.*;
import static io.art.tarantool.module.TarantoolModule.*;
import static org.msgpack.value.ValueFactory.*;
import static reactor.core.publisher.Flux.*;
import java.util.*;

@Public
@RequiredArgsConstructor
public class TarantoolReactiveStorageService<KeyType, SpaceType> implements ReactiveSpaceService<KeyType, SpaceType> {
    private final TarantoolModelReader reader;
    private final TarantoolUpdateSerializer updateSerializer;
    private final ImmutableStringValue spaceName;
    private final MetaType<SpaceType> spaceMetaType;
    private final TarantoolStorageConnector connector;
    private final TarantoolModelWriter writer;
    private final MetaType<KeyType> keyMeta;
    private final TarantoolReactiveRouterService<KeyType, SpaceType> sharded;
    private final TarantoolReactiveStorageIndexService<SpaceType> index;

    public TarantoolReactiveStorageService(MetaType<KeyType> keyMeta, MetaClass<SpaceType> spaceMeta, TarantoolStorageConnector connector) {
        this.connector = connector;
        this.spaceMetaType = spaceMeta.definition();
        this.keyMeta = keyMeta;
        this.spaceName = newString(idByDash(spaceMeta.definition().type()));
        writer = tarantoolModule().configuration().getWriter();
        reader = tarantoolModule().configuration().getReader();
        updateSerializer = new TarantoolUpdateSerializer(writer);
        sharded = new TarantoolReactiveRouterService<>(keyMeta, spaceMeta, connector);
        index = new TarantoolReactiveStorageIndexService<>(spaceMetaType, spaceName, connector);
    }

    @Override
    public ReactiveShardService<KeyType, SpaceType> shard(ShardRequest request) {
        return sharded.sharded(request);
    }

    @Override
    public final ReactiveIndexService<SpaceType> index(Index index) {
        return this.index.indexed(index);
    }

    @Override
    public Mono<SpaceType> first(KeyType key) {
        ArrayValue input = newArray(spaceName, writer.write(keyMeta, key));
        Mono<Value> output = connector.immutable().call(SPACE_FIRST, input);
        return parseSpaceMono(output);
    }

    @Override
    public Flux<SpaceType> select(KeyType key) {
        ArrayValue input = newArray(spaceName, writer.write(keyMeta, key));
        Mono<Value> output = connector.immutable().call(SPACE_SELECT, input);
        return parseSpaceFlux(output);
    }

    @Override
    public Flux<SpaceType> select(KeyType key, long offset, long limit) {
        ArrayValue input = newArray(spaceName, writer.write(keyMeta, key), newArray(newInteger(offset), newInteger(limit)));
        Mono<Value> output = connector.immutable().call(SPACE_SELECT, input);
        return parseSpaceFlux(output);
    }

    @Override
    public Flux<SpaceType> find(Collection<KeyType> keys) {
        ArrayValue input = newArray(spaceName, newArray(keys.stream().map(key -> writer.write(keyMeta, key)).collect(listCollector())));
        Mono<Value> output = connector.immutable().call(SPACE_FIND, input);
        return parseSpaceFlux(output);
    }

    @Override
    public Flux<SpaceType> find(ImmutableCollection<KeyType> keys) {
        ArrayValue input = newArray(spaceName, newArray(keys.stream().map(key -> writer.write(keyMeta, key)).collect(listCollector())));
        Mono<Value> output = connector.immutable().call(SPACE_FIND, input);
        return parseSpaceFlux(output);
    }


    @Override
    public Mono<SpaceType> delete(KeyType key) {
        ArrayValue input = newArray(spaceName, writer.write(keyMeta, key));
        Mono<Value> output = connector.immutable().call(SPACE_SINGLE_DELETE, input);
        return parseSpaceMono(output);
    }

    @Override
    public Flux<SpaceType> delete(Collection<KeyType> keys) {
        ArrayValue input = newArray(spaceName, newArray(keys.stream().map(key -> writer.write(keyMeta, key)).collect(listCollector())));
        Mono<Value> output = connector.immutable().call(SPACE_MULTIPLE_DELETE, input);
        return parseSpaceFlux(output);
    }

    @Override
    public Flux<SpaceType> delete(ImmutableCollection<KeyType> keys) {
        ArrayValue input = newArray(spaceName, newArray(keys.stream().map(key -> writer.write(keyMeta, key)).collect(listCollector())));
        Mono<Value> output = connector.immutable().call(SPACE_MULTIPLE_DELETE, input);
        return parseSpaceFlux(output);
    }


    @Override
    public Mono<SpaceType> insert(SpaceType value) {
        ArrayValue input = newArray(spaceName, writer.write(spaceMetaType, value));
        Mono<Value> output = connector.mutable().call(SPACE_SINGLE_INSERT, input);
        return parseSpaceMono(output);
    }

    @Override
    public Flux<SpaceType> insert(Collection<SpaceType> value) {
        ArrayValue input = newArray(spaceName, newArray(value.stream().map(element -> writer.write(spaceMetaType, element)).collect(listCollector())));
        Mono<Value> output = connector.mutable().call(SPACE_MULTIPLE_INSERT, input);
        return parseSpaceFlux(output);
    }

    @Override
    public Flux<SpaceType> insert(ImmutableCollection<SpaceType> value) {
        ArrayValue input = newArray(spaceName, newArray(value.stream().map(element -> writer.write(spaceMetaType, element)).collect(listCollector())));
        Mono<Value> output = connector.mutable().call(SPACE_MULTIPLE_INSERT, input);
        return parseSpaceFlux(output);
    }


    @Override
    public Mono<SpaceType> put(SpaceType value) {
        ArrayValue input = newArray(spaceName, writer.write(spaceMetaType, value));
        Mono<Value> output = connector.mutable().call(SPACE_SINGLE_PUT, input);
        return parseSpaceMono(output);
    }

    @Override
    public Flux<SpaceType> put(Collection<SpaceType> value) {
        ArrayValue input = newArray(spaceName, newArray(value.stream().map(element -> writer.write(spaceMetaType, element)).collect(listCollector())));
        Mono<Value> output = connector.mutable().call(SPACE_MULTIPLE_PUT, input);
        return parseSpaceFlux(output);
    }

    @Override
    public Flux<SpaceType> put(ImmutableCollection<SpaceType> value) {
        ArrayValue input = newArray(spaceName, newArray(value.stream().map(element -> writer.write(spaceMetaType, element)).collect(listCollector())));
        Mono<Value> output = connector.mutable().call(SPACE_MULTIPLE_PUT, input);
        return parseSpaceFlux(output);
    }

    @Override
    public Mono<SpaceType> update(KeyType key, Updater<SpaceType> updater) {
        ArrayValue input = newArray(spaceName, writer.write(keyMeta, key), updateSerializer.serializeUpdate(cast(updater)));
        Mono<Value> output = connector.mutable().call(SPACE_SINGLE_UPDATE, input);
        return parseSpaceMono(output);
    }

    @Override
    public Mono<Void> upsert(SpaceType model, Updater<SpaceType> updater) {
        ArrayValue input = newArray(spaceName, writer.write(spaceMetaType, model), updateSerializer.serializeUpdate(cast(updater)));
        Mono<Value> output = connector.mutable().call(SPACE_SINGLE_UPSERT, input);
        return output.then();
    }

    @Override
    public Flux<SpaceType> update(Collection<KeyType> keys, Updater<SpaceType> updater) {
        List<Value> serializedKeys = keys.stream().map(key -> writer.write(keyMeta, key)).collect(listCollector());
        ArrayValue input = newArray(spaceName, newArray(serializedKeys), updateSerializer.serializeUpdate(cast(updater)));
        Mono<Value> output = connector.mutable().call(SPACE_MULTIPLE_UPDATE, input);
        return parseSpaceFlux(output);
    }

    @Override
    public Flux<SpaceType> update(ImmutableCollection<KeyType> keys, Updater<SpaceType> updater) {
        List<Value> serializedKeys = keys.stream().map(key -> writer.write(keyMeta, key)).collect(listCollector());
        ArrayValue input = newArray(spaceName, newArray(serializedKeys), updateSerializer.serializeUpdate(cast(updater)));
        Mono<Value> output = connector.mutable().call(SPACE_MULTIPLE_UPDATE, input);
        return parseSpaceFlux(output);
    }

    @Override
    public Mono<Long> count(KeyType key) {
        Mono<Value> output = connector.immutable().call(SPACE_COUNT, newArray(spaceName, writer.write(keyMeta, key)));
        return parseLongMono(output);
    }

    @Override
    public Mono<Long> size() {
        Mono<Value> output = connector.immutable().call(SPACE_COUNT, newArray(spaceName));
        return parseLongMono(output);
    }

    @Override
    public Mono<Void> truncate() {
        return connector.mutable().call(SPACE_TRUNCATE, newArray(spaceName)).then();
    }

    @Override
    public TarantoolReactiveStorageSpaceStream<SpaceType> stream() {
        return TarantoolReactiveStorageSpaceStream.<SpaceType>builder()
                .spaceName(spaceName)
                .spaceType(spaceMetaType)
                .connector(connector)
                .build();
    }

    @Override
    public TarantoolReactiveStorageSpaceStream<SpaceType> stream(KeyType baseKey) {
        return TarantoolReactiveStorageSpaceStream.<SpaceType>builder()
                .spaceName(spaceName)
                .spaceType(spaceMetaType)
                .connector(connector)
                .baseKey(tuple(baseKey))
                .build();
    }

    private Mono<Long> parseLongMono(Mono<Value> value) {
        return value.map(element -> reader.read(longType(), element));
    }

    private Flux<SpaceType> parseSpaceFlux(Mono<Value> value) {
        return value.flatMapMany(elements -> fromStream(elements.asArrayValue()
                .list()
                .stream()
                .map(element -> reader.read(spaceMetaType, element))));
    }

    private Mono<SpaceType> parseSpaceMono(Mono<Value> value) {
        return value.map(element -> reader.read(spaceMetaType, element));
    }

}
