package io.art.tarantool.service.index;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.core.model.*;
import io.art.meta.model.*;
import io.art.storage.constants.*;
import io.art.storage.index.*;
import io.art.storage.service.*;
import io.art.storage.sharder.*;
import io.art.storage.updater.*;
import io.art.tarantool.connector.*;
import io.art.tarantool.descriptor.*;
import io.art.tarantool.serializer.*;
import io.art.tarantool.stream.*;
import lombok.*;
import org.msgpack.value.Value;
import org.msgpack.value.*;
import reactor.core.publisher.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.collector.ArrayCollector.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.meta.Meta.*;
import static io.art.meta.registry.BuiltinMetaTypes.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.Functions.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.ShardingAlgorithmCode.*;
import static io.art.tarantool.module.TarantoolModule.*;
import static org.msgpack.value.ValueFactory.*;
import static reactor.core.publisher.Flux.*;
import java.util.*;

@Public
public class TarantoolReactiveRouterIndexService<SpaceType> implements ReactiveIndexService<SpaceType> {
    private final ImmutableStringValue spaceName;
    private final TarantoolStorageConnector connector;
    private final TarantoolModelWriter writer;
    private final TarantoolModelReader reader;
    private final MetaType<SpaceType> spaceType;
    private final TarantoolUpdateSerializer updateSerializer;
    private final ThreadLocal<ShardRequest> shard = new ThreadLocal<>();
    private final ThreadLocal<Index> index = new ThreadLocal<>();

    @Builder
    public TarantoolReactiveRouterIndexService(MetaType<SpaceType> spaceType, ImmutableStringValue spaceName, TarantoolStorageConnector connector) {
        this.spaceType = spaceType;
        this.connector = connector;
        this.spaceName = spaceName;
        writer = tarantoolModule().configuration().getWriter();
        reader = tarantoolModule().configuration().getReader();
        updateSerializer = new TarantoolUpdateSerializer(writer);
    }

    public TarantoolReactiveRouterIndexService<SpaceType> sharded(ShardRequest request) {
        shard.set(request);
        return this;
    }

    public TarantoolReactiveRouterIndexService<SpaceType> indexed(Index index) {
        this.index.set(index);
        return this;
    }

    @Override
    public Mono<SpaceType> first(Tuple tuple) {
        ImmutableArrayValue input = newArray(spaceName, newString(this.index.get().name()), serializeTuple(tuple));
        Mono<Value> output = connector.router().call(INDEX_FIRST, writeRequest(input));
        return readSpaceMono(output);
    }

    @Override
    public Flux<SpaceType> select(Tuple tuple) {
        ImmutableArrayValue input = newArray(spaceName, newString(this.index.get().name()), serializeTuple(tuple));
        Mono<Value> output = connector.router().call(INDEX_SELECT, writeRequest(input));
        return readSpaceFlux(output);
    }

    @Override
    public Flux<SpaceType> select(Tuple tuple, int offset, int limit) {
        ImmutableArrayValue input = newArray(spaceName, newString(this.index.get().name()), serializeTuple(tuple), newArray(newInteger(offset), newInteger(limit)));
        Mono<Value> output = connector.router().call(INDEX_SELECT, writeRequest(input));
        return readSpaceFlux(output);
    }

    @Override
    public Mono<SpaceType> update(Tuple key, Updater<SpaceType> updater) {
        if (key.size() == 1) {
            ImmutableArrayValue input = newArray(spaceName, newString(this.index.get().name()), serializeTuple(key), updateSerializer.serializeUpdate(cast(updater)));
            Mono<Value> output = connector.router().call(INDEX_SINGLE_UPDATE, writeRequest(input));
            return readSpaceMono(output);
        }
        return update(linkedListOf(key), updater).next();
    }

    @Override
    public Flux<SpaceType> update(Collection<? extends Tuple> keys, Updater<SpaceType> updater) {
        ImmutableArrayValue input = newArray(
                spaceName,
                newString(this.index.get().name()),
                newArray(keys.stream().map(this::serializeTuple).collect(listCollector())),
                updateSerializer.serializeUpdate(cast(updater))
        );
        Mono<Value> output = connector.router().call(INDEX_MULTIPLE_UPDATE, writeRequest(input));
        return readSpaceFlux(output);
    }

    @Override
    public Flux<SpaceType> update(ImmutableCollection<? extends Tuple> keys, Updater<SpaceType> updater) {
        ImmutableArrayValue input = newArray(
                spaceName,
                newString(this.index.get().name()),
                newArray(keys.stream().map(this::serializeTuple).collect(listCollector())),
                updateSerializer.serializeUpdate(cast(updater))
        );
        Mono<Value> output = connector.router().call(INDEX_MULTIPLE_UPDATE, writeRequest(input));
        return readSpaceFlux(output);
    }

    @Override
    public Flux<SpaceType> find(Collection<? extends Tuple> keys) {
        ImmutableArrayValue serializedKeys = newArray(keys.stream().map(this::serializeTuple).collect(listCollector()));
        ImmutableArrayValue input = newArray(spaceName, newString(this.index.get().name()), serializedKeys);
        Mono<Value> output = connector.router().call(INDEX_FIND, writeRequest(input));
        return readSpaceFlux(output);
    }

    @Override
    public Flux<SpaceType> find(ImmutableCollection<? extends Tuple> keys) {
        ImmutableArrayValue serializedKeys = newArray(keys.stream().map(this::serializeTuple).collect(listCollector()));
        ImmutableArrayValue input = newArray(spaceName, newString(this.index.get().name()), serializedKeys);
        Mono<Value> output = connector.router().call(INDEX_FIND, writeRequest(input));
        return readSpaceFlux(output);
    }

    @Override
    public Mono<SpaceType> delete(Tuple key) {
        if (key.size() == 1) {
            ImmutableArrayValue input = newArray(spaceName, newString(this.index.get().name()), serializeTuple(key));
            Mono<Value> output = connector.router().call(INDEX_SINGLE_DELETE, writeRequest(input));
            return readSpaceMono(output);
        }
        return delete(linkedListOf(key)).next();
    }

    @Override
    public Flux<SpaceType> delete(Collection<? extends Tuple> keys) {
        ImmutableArrayValue serializedKeys = newArray(keys.stream().map(this::serializeTuple).collect(listCollector()));
        ImmutableArrayValue input = newArray(spaceName, newString(this.index.get().name()), serializedKeys);
        Mono<Value> output = connector.router().call(INDEX_MULTIPLE_DELETE, writeRequest(input));
        return readSpaceFlux(output);
    }

    @Override
    public Flux<SpaceType> delete(ImmutableCollection<? extends Tuple> keys) {
        ImmutableArrayValue serializedKeys = newArray(keys.stream().map(this::serializeTuple).collect(listCollector()));
        ImmutableArrayValue input = newArray(spaceName, newString(this.index.get().name()), serializedKeys);
        Mono<Value> output = connector.router().call(INDEX_MULTIPLE_DELETE, writeRequest(input));
        return readSpaceFlux(output);
    }

    @Override
    public Mono<Long> count(Tuple tuple) {
        ImmutableArrayValue input = newArray(spaceName, newString(this.index.get().name()), serializeTuple(tuple));
        return readCountMono(connector.router().call(INDEX_COUNT, writeRequest(input)));
    }

    @Override
    public TarantoolReactiveRouterIndexStream<SpaceType> stream() {
        return TarantoolReactiveRouterIndexStream.<SpaceType>builder()
                .spaceName(spaceName)
                .indexName(newString(this.index.get().name()))
                .spaceType(spaceType)
                .connector(connector)
                .build();
    }

    @Override
    public TarantoolReactiveRouterIndexStream<SpaceType> stream(Tuple baseKey) {
        return TarantoolReactiveRouterIndexStream.<SpaceType>builder()
                .spaceName(spaceName)
                .indexName(newString(this.index.get().name()))
                .spaceType(spaceType)
                .connector(connector)
                .baseKey(baseKey)
                .build();
    }

    private ImmutableArrayValue serializeTuple(Tuple tuple) {
        List<Value> serialized = linkedList();
        int index = 0;
        for (Object key : tuple.values()) {
            serialized.add(writer.write(this.index.get().fields().get(index++).type(), key));
        }
        return newArray(serialized);
    }

    private ImmutableArrayValue writeRequest(ImmutableValue input) {
        ShardRequest shardRequest = this.shard.get();
        ImmutableArrayValue shardData = newArray(shardRequest.getData()
                .values()
                .stream()
                .map(element -> writer.write(definition(element.getClass()), element))
                .toArray(Value[]::new), true);
        ImmutableIntegerValue algorithm = newInteger(0);
        if (shardRequest.getAlgorithm() == StorageConstants.ShardingAlgorithm.CRC_32) algorithm = CRC_32;
        return newArray(newArray(algorithm, shardData), input);
    }

    private Mono<Long> readCountMono(Mono<Value> value) {
        return value.map(element -> reader.read(longType(), element));
    }

    private Mono<SpaceType> readSpaceMono(Mono<Value> value) {
        return value.map(element -> reader.read(spaceType, element));
    }

    private Flux<SpaceType> readSpaceFlux(Mono<Value> value) {
        return value.flatMapMany(elements -> fromStream(elements.asArrayValue()
                .list()
                .stream()
                .map(element -> reader.read(spaceType, element))));
    }
}
