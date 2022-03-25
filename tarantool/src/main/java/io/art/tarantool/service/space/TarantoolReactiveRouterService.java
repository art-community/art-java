package io.art.tarantool.service.space;

import io.art.core.collection.*;
import io.art.meta.model.*;
import io.art.storage.constants.StorageConstants.*;
import io.art.storage.index.*;
import io.art.storage.service.*;
import io.art.storage.sharder.*;
import io.art.storage.updater.*;
import io.art.tarantool.connector.*;
import io.art.tarantool.descriptor.*;
import io.art.tarantool.serializer.*;
import io.art.tarantool.service.index.*;
import io.art.tarantool.stream.*;
import org.msgpack.value.*;
import reactor.core.publisher.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.collector.ArrayCollector.*;
import static io.art.core.model.Tuple.*;
import static io.art.core.normalizer.ClassIdentifierNormalizer.*;
import static io.art.meta.Meta.*;
import static io.art.meta.registry.BuiltinMetaTypes.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.Functions.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.ShardingAlgorhtim.*;
import static io.art.tarantool.module.TarantoolModule.*;
import static org.msgpack.value.ValueFactory.*;
import static reactor.core.publisher.Flux.*;
import java.util.*;

public class TarantoolReactiveRouterService<KeyType, SpaceType> implements ReactiveShardService<KeyType, SpaceType> {
    private final TarantoolModelReader reader;
    private final TarantoolModelWriter writer;
    private final TarantoolUpdateSerializer updateSerializer;
    private final ImmutableStringValue spaceName;
    private final MetaType<SpaceType> spaceMetaType;
    private final TarantoolStorageConnector connector;
    private final MetaType<KeyType> keyMeta;
    private final ThreadLocal<ShardRequest> shard = new ThreadLocal<>();
    private final TarantoolReactiveRouterIndexService<SpaceType> index;

    public TarantoolReactiveRouterService(MetaType<KeyType> keyMeta, MetaClass<SpaceType> spaceMeta, TarantoolStorageConnector connector) {
        this.connector = connector;
        this.spaceMetaType = spaceMeta.definition();
        this.keyMeta = keyMeta;
        this.spaceName = newString(idByDash(spaceMeta.definition().type()));
        writer = tarantoolModule().configuration().getWriter();
        reader = tarantoolModule().configuration().getReader();
        updateSerializer = new TarantoolUpdateSerializer(writer);
        index = new TarantoolReactiveRouterIndexService<>(spaceMetaType, spaceName, connector);
    }

    public TarantoolReactiveRouterService<KeyType, SpaceType> sharded(ShardRequest request) {
        shard.set(request);
        index.sharded(request);
        return this;
    }

    @Override
    public TarantoolReactiveRouterIndexService<SpaceType> index(Index index) {
        return this.index.indexed(index);
    }

    @Override
    public TarantoolReactiveRouterSpaceStream<SpaceType> stream() {
        return TarantoolReactiveRouterSpaceStream.<SpaceType>builder()
                .shardRequest(this.shard.get())
                .spaceName(spaceName)
                .spaceType(spaceMetaType)
                .connector(connector)
                .build();
    }

    @Override
    public TarantoolReactiveRouterSpaceStream<SpaceType> stream(KeyType baseKey) {
        return TarantoolReactiveRouterSpaceStream.<SpaceType>builder()
                .shardRequest(this.shard.get())
                .spaceName(spaceName)
                .spaceType(spaceMetaType)
                .connector(connector)
                .baseKey(tuple(baseKey))
                .build();
    }


    @Override
    public Mono<SpaceType> first(KeyType key) {
        ImmutableArrayValue input = newArray(spaceName, writer.write(keyMeta, key));
        Mono<Value> output = connector.router().call(SPACE_FIRST, writeRequest(input));
        return readSpaceMono(output);
    }

    @Override
    public Flux<SpaceType> select(KeyType key) {
        ImmutableArrayValue input = newArray(spaceName, writer.write(keyMeta, key));
        Mono<Value> output = connector.router().call(SPACE_SELECT, writeRequest(input));
        return readSpaceFlux(output);
    }

    @Override
    public Flux<SpaceType> select(KeyType key, long offset, long limit) {
        ImmutableArrayValue input = newArray(spaceName, writer.write(keyMeta, key), newArray(newInteger(offset), newInteger(limit)));
        Mono<Value> output = connector.router().call(SPACE_SELECT, writeRequest(input));
        return readSpaceFlux(output);
    }

    @Override
    public Flux<SpaceType> find(Collection<KeyType> keys) {
        ImmutableArrayValue input = newArray(spaceName, newArray(keys.stream().map(key -> writer.write(keyMeta, key)).collect(listCollector())));
        Mono<Value> output = connector.router().call(SPACE_FIND, writeRequest(input));
        return readSpaceFlux(output);
    }

    @Override
    public Flux<SpaceType> find(ImmutableCollection<KeyType> keys) {
        ImmutableArrayValue input = newArray(spaceName, newArray(keys.stream().map(key -> writer.write(keyMeta, key)).collect(listCollector())));
        Mono<Value> output = connector.router().call(SPACE_FIND, writeRequest(input));
        return readSpaceFlux(output);
    }


    @Override
    public Mono<SpaceType> delete(KeyType key) {
        ImmutableArrayValue input = newArray(spaceName, writer.write(keyMeta, key));
        Mono<Value> output = connector.router().call(SPACE_SINGLE_DELETE, writeRequest(input));
        return readSpaceMono(output);
    }

    @Override
    public Flux<SpaceType> delete(Collection<KeyType> keys) {
        ImmutableArrayValue input = newArray(spaceName, newArray(keys.stream().map(key -> writer.write(keyMeta, key)).collect(listCollector())));
        Mono<Value> output = connector.router().call(SPACE_MULTIPLE_DELETE, writeRequest(input));
        return readSpaceFlux(output);
    }

    @Override
    public Flux<SpaceType> delete(ImmutableCollection<KeyType> keys) {
        ImmutableArrayValue input = newArray(spaceName, newArray(keys.stream().map(key -> writer.write(keyMeta, key)).collect(listCollector())));
        Mono<Value> output = connector.router().call(SPACE_MULTIPLE_DELETE, writeRequest(input));
        return readSpaceFlux(output);
    }


    @Override
    public Mono<SpaceType> insert(SpaceType value) {
        ImmutableArrayValue input = newArray(spaceName, writer.write(spaceMetaType, value));
        Mono<Value> output = connector.router().call(SPACE_SINGLE_INSERT, writeRequest(input));
        return readSpaceMono(output);
    }

    @Override
    public Flux<SpaceType> insert(Collection<SpaceType> value) {
        ImmutableArrayValue input = newArray(spaceName, newArray(value.stream().map(element -> writer.write(spaceMetaType, element)).collect(listCollector())));
        Mono<Value> output = connector.router().call(SPACE_MULTIPLE_INSERT, writeRequest(input));
        return readSpaceFlux(output);
    }

    @Override
    public Flux<SpaceType> insert(ImmutableCollection<SpaceType> value) {
        ImmutableArrayValue input = newArray(spaceName, newArray(value.stream().map(element -> writer.write(spaceMetaType, element)).collect(listCollector())));
        Mono<Value> output = connector.router().call(SPACE_MULTIPLE_INSERT, writeRequest(input));
        return readSpaceFlux(output);
    }


    @Override
    public Mono<SpaceType> put(SpaceType value) {
        ImmutableArrayValue input = newArray(spaceName, writer.write(spaceMetaType, value));
        Mono<Value> output = connector.router().call(SPACE_SINGLE_PUT, writeRequest(input));
        return readSpaceMono(output);
    }

    @Override
    public Flux<SpaceType> put(Collection<SpaceType> value) {
        ImmutableArrayValue input = newArray(spaceName, newArray(value.stream().map(element -> writer.write(spaceMetaType, element)).collect(listCollector())));
        Mono<Value> output = connector.router().call(SPACE_MULTIPLE_PUT, writeRequest(input));
        return readSpaceFlux(output);
    }

    @Override
    public Flux<SpaceType> put(ImmutableCollection<SpaceType> value) {
        ImmutableArrayValue input = newArray(spaceName, newArray(value.stream().map(element -> writer.write(spaceMetaType, element)).collect(listCollector())));
        Mono<Value> output = connector.router().call(SPACE_MULTIPLE_PUT, writeRequest(input));
        return readSpaceFlux(output);
    }

    @Override
    public Mono<SpaceType> update(KeyType key, Updater<SpaceType> updater) {
        ImmutableArrayValue input = newArray(spaceName, writer.write(keyMeta, key), updateSerializer.serializeUpdate(cast(updater)));
        Mono<Value> output = connector.router().call(SPACE_SINGLE_UPDATE, writeRequest(input));
        return readSpaceMono(output);
    }

    @Override
    public Mono<Void> upsert(SpaceType model, Updater<SpaceType> updater) {
        ImmutableArrayValue input = newArray(spaceName, writer.write(spaceMetaType, model), updateSerializer.serializeUpdate(cast(updater)));
        Mono<Value> output = connector.router().call(SPACE_SINGLE_UPDATE, writeRequest(input));
        return output.then();
    }

    @Override
    public Flux<SpaceType> update(Collection<KeyType> keys, Updater<SpaceType> updater) {
        List<Value> serializedKeys = keys.stream().map(key -> writer.write(keyMeta, key)).collect(listCollector());
        ImmutableArrayValue input = newArray(spaceName, newArray(serializedKeys), updateSerializer.serializeUpdate(cast(updater)));
        Mono<Value> output = connector.router().call(SPACE_MULTIPLE_UPDATE, writeRequest(input));
        return readSpaceFlux(output);
    }

    @Override
    public Flux<SpaceType> update(ImmutableCollection<KeyType> keys, Updater<SpaceType> updater) {
        List<Value> serializedKeys = keys.stream().map(key -> writer.write(keyMeta, key)).collect(listCollector());
        ImmutableArrayValue input = newArray(spaceName, newArray(serializedKeys), updateSerializer.serializeUpdate(cast(updater)));
        Mono<Value> output = connector.router().call(SPACE_MULTIPLE_UPDATE, writeRequest(input));
        return readSpaceFlux(output);
    }

    @Override
    public Mono<Long> count(KeyType key) {
        Mono<Value> output = connector.router().call(SPACE_COUNT, writeRequest(newArray(spaceName, writer.write(keyMeta, key))));
        return readLongMono(output);
    }

    @Override
    public Mono<Long> size() {
        Mono<Value> output = connector.router().call(SPACE_COUNT, writeRequest(newArray(spaceName)));
        return readLongMono(output);
    }

    @Override
    public Mono<Void> truncate() {
        return connector.router().call(SPACE_TRUNCATE, writeRequest(newArray(spaceName))).then();
    }

    private ImmutableArrayValue writeRequest(ImmutableValue input) {
        ShardRequest request = this.shard.get();
        ImmutableArrayValue shardData = newArray(request.getData()
                .values()
                .stream()
                .map(element -> writer.write(definition(element.getClass()), element))
                .toArray(Value[]::new), true);
        ImmutableIntegerValue algorithm = newInteger(0);
        if (request.getAlgorithm() == ShardingAlgorithm.CRC_32) algorithm = CRC_32;
        return newArray(newArray(algorithm, shardData), input);
    }

    private Mono<Long> readLongMono(Mono<Value> value) {
        return value.map(element -> reader.read(longType(), element));
    }

    private Flux<SpaceType> readSpaceFlux(Mono<Value> value) {
        return value.flatMapMany(elements -> fromStream(elements.asArrayValue()
                .list()
                .stream()
                .map(element -> reader.read(spaceMetaType, element))));
    }

    private Mono<SpaceType> readSpaceMono(Mono<Value> value) {
        return value.map(element -> reader.read(spaceMetaType, element));
    }
}
