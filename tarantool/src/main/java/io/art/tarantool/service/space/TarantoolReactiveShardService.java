package io.art.tarantool.service.space;

import io.art.core.collection.*;
import io.art.meta.model.*;
import io.art.storage.constants.StorageConstants.*;
import io.art.storage.service.*;
import io.art.storage.sharder.*;
import io.art.storage.updater.*;
import io.art.tarantool.descriptor.*;
import io.art.tarantool.registry.*;
import io.art.tarantool.serializer.*;
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

public class TarantoolReactiveShardService<KeyType, ModelType> implements ReactiveShardService<KeyType, ModelType> {
    private final TarantoolModelReader reader;
    private final TarantoolModelWriter writer;
    private final TarantoolUpdateSerializer updateSerializer;
    private final ImmutableStringValue spaceName;
    private final MetaType<ModelType> spaceMetaType;
    private final TarantoolClientRegistry clients;
    private final MetaType<KeyType> keyMeta;
    private final ThreadLocal<ShardRequest> shard = new ThreadLocal<>();

    public TarantoolReactiveShardService(MetaType<KeyType> keyMeta, MetaClass<ModelType> spaceMeta, TarantoolClientRegistry clients) {
        this.clients = clients;
        this.spaceMetaType = spaceMeta.definition();
        this.keyMeta = keyMeta;
        this.spaceName = newString(idByDash(spaceMeta.definition().type()));
        writer = tarantoolModule().configuration().getWriter();
        reader = tarantoolModule().configuration().getReader();
        updateSerializer = new TarantoolUpdateSerializer(writer);
    }

    TarantoolReactiveShardService<KeyType, ModelType> shard(ShardRequest request) {
        shard.set(request);
        return this;
    }

    @Override
    public Mono<ModelType> first(KeyType key) {
        ImmutableArrayValue input = newArray(spaceName, writer.write(keyMeta, key));
        Mono<Value> output = clients.router().call(SPACE_FIRST, writeRequest(input));
        return readSpaceMono(output);
    }

    @Override
    public Flux<ModelType> select(KeyType key) {
        ImmutableArrayValue input = newArray(spaceName, writer.write(keyMeta, key));
        Mono<Value> output = clients.router().call(SPACE_SELECT, writeRequest(input));
        return readSpaceFlux(output);
    }

    @Override
    public Flux<ModelType> select(KeyType key, long offset, long limit) {
        ImmutableArrayValue input = newArray(spaceName, writer.write(keyMeta, key), newArray(newInteger(offset), newInteger(limit)));
        Mono<Value> output = clients.router().call(SPACE_SELECT, writeRequest(input));
        return readSpaceFlux(output);
    }

    @Override
    public Flux<ModelType> find(Collection<KeyType> keys) {
        ImmutableArrayValue input = newArray(spaceName, newArray(keys.stream().map(key -> writer.write(keyMeta, key)).collect(listCollector())));
        Mono<Value> output = clients.router().call(SPACE_FIND, writeRequest(input));
        return readSpaceFlux(output);
    }

    @Override
    public Flux<ModelType> find(ImmutableCollection<KeyType> keys) {
        ImmutableArrayValue input = newArray(spaceName, newArray(keys.stream().map(key -> writer.write(keyMeta, key)).collect(listCollector())));
        Mono<Value> output = clients.router().call(SPACE_FIND, writeRequest(input));
        return readSpaceFlux(output);
    }


    @Override
    public Mono<ModelType> delete(KeyType key) {
        ImmutableArrayValue input = newArray(spaceName, writer.write(keyMeta, key));
        Mono<Value> output = clients.router().call(SPACE_SINGLE_DELETE, writeRequest(input));
        return readSpaceMono(output);
    }

    @Override
    public Flux<ModelType> delete(Collection<KeyType> keys) {
        ImmutableArrayValue input = newArray(spaceName, newArray(keys.stream().map(key -> writer.write(keyMeta, key)).collect(listCollector())));
        Mono<Value> output = clients.router().call(SPACE_MULTIPLE_DELETE, writeRequest(input));
        return readSpaceFlux(output);
    }

    @Override
    public Flux<ModelType> delete(ImmutableCollection<KeyType> keys) {
        ImmutableArrayValue input = newArray(spaceName, newArray(keys.stream().map(key -> writer.write(keyMeta, key)).collect(listCollector())));
        Mono<Value> output = clients.router().call(SPACE_MULTIPLE_DELETE, writeRequest(input));
        return readSpaceFlux(output);
    }


    @Override
    public Mono<ModelType> insert(ModelType value) {
        ImmutableArrayValue input = newArray(spaceName, writer.write(spaceMetaType, value));
        Mono<Value> output = clients.router().call(SPACE_SINGLE_INSERT, writeRequest(input));
        return readSpaceMono(output);
    }

    @Override
    public Flux<ModelType> insert(Collection<ModelType> value) {
        ImmutableArrayValue input = newArray(spaceName, newArray(value.stream().map(element -> writer.write(spaceMetaType, element)).collect(listCollector())));
        Mono<Value> output = clients.router().call(SPACE_MULTIPLE_INSERT, writeRequest(input));
        return readSpaceFlux(output);
    }

    @Override
    public Flux<ModelType> insert(ImmutableCollection<ModelType> value) {
        ImmutableArrayValue input = newArray(spaceName, newArray(value.stream().map(element -> writer.write(spaceMetaType, element)).collect(listCollector())));
        Mono<Value> output = clients.router().call(SPACE_MULTIPLE_INSERT, writeRequest(input));
        return readSpaceFlux(output);
    }


    @Override
    public Mono<ModelType> put(ModelType value) {
        ImmutableArrayValue input = newArray(spaceName, writer.write(spaceMetaType, value));
        Mono<Value> output = clients.router().call(SPACE_SINGLE_PUT, writeRequest(input));
        return readSpaceMono(output);
    }

    @Override
    public Flux<ModelType> put(Collection<ModelType> value) {
        ImmutableArrayValue input = newArray(spaceName, newArray(value.stream().map(element -> writer.write(spaceMetaType, element)).collect(listCollector())));
        Mono<Value> output = clients.router().call(SPACE_MULTIPLE_PUT, writeRequest(input));
        return readSpaceFlux(output);
    }

    @Override
    public Flux<ModelType> put(ImmutableCollection<ModelType> value) {
        ImmutableArrayValue input = newArray(spaceName, newArray(value.stream().map(element -> writer.write(spaceMetaType, element)).collect(listCollector())));
        Mono<Value> output = clients.router().call(SPACE_MULTIPLE_PUT, writeRequest(input));
        return readSpaceFlux(output);
    }

    @Override
    public Mono<ModelType> update(KeyType key, Updater<ModelType> updater) {
        ImmutableArrayValue input = newArray(spaceName, writer.write(keyMeta, key), updateSerializer.serializeUpdate(cast(updater)));
        Mono<Value> output = clients.router().call(SPACE_SINGLE_UPDATE, writeRequest(input));
        return readSpaceMono(output);
    }

    @Override
    public Mono<Void> upsert(ModelType model, Updater<ModelType> updater) {
        ImmutableArrayValue input = newArray(spaceName, writer.write(spaceMetaType, model), updateSerializer.serializeUpdate(cast(updater)));
        Mono<Value> output = clients.router().call(SPACE_SINGLE_UPDATE, writeRequest(input));
        return output.then();
    }

    @Override
    public Flux<ModelType> update(Collection<KeyType> keys, Updater<ModelType> updater) {
        List<Value> serializedKeys = keys.stream().map(key -> writer.write(keyMeta, key)).collect(listCollector());
        ImmutableArrayValue input = newArray(spaceName, newArray(serializedKeys), updateSerializer.serializeUpdate(cast(updater)));
        Mono<Value> output = clients.router().call(SPACE_MULTIPLE_UPDATE, writeRequest(input));
        return readSpaceFlux(output);
    }

    @Override
    public Flux<ModelType> update(ImmutableCollection<KeyType> keys, Updater<ModelType> updater) {
        List<Value> serializedKeys = keys.stream().map(key -> writer.write(keyMeta, key)).collect(listCollector());
        ImmutableArrayValue input = newArray(spaceName, newArray(serializedKeys), updateSerializer.serializeUpdate(cast(updater)));
        Mono<Value> output = clients.router().call(SPACE_MULTIPLE_UPDATE, writeRequest(input));
        return readSpaceFlux(output);
    }

    @Override
    public Mono<Long> count(KeyType key) {
        Mono<Value> output = clients.router().call(SPACE_COUNT, writeRequest(newArray(spaceName, writer.write(keyMeta, key))));
        return readLongMono(output);
    }

    @Override
    public Mono<Long> size() {
        Mono<Value> output = clients.router().call(SPACE_COUNT, writeRequest(newArray(spaceName)));
        return readLongMono(output);
    }

    @Override
    public Mono<Void> truncate() {
        return clients.router().call(SPACE_TRUNCATE, writeRequest(newArray(spaceName))).then();
    }

    @Override
    public TarantoolReactiveSpaceStream<ModelType> stream() {
        return TarantoolReactiveSpaceStream.<ModelType>builder()
                .spaceName(spaceName)
                .spaceType(spaceMetaType)
                .clients(clients)
                .build();
    }

    @Override
    public TarantoolReactiveSpaceStream<ModelType> stream(KeyType baseKey) {
        return TarantoolReactiveSpaceStream.<ModelType>builder()
                .spaceName(spaceName)
                .spaceType(spaceMetaType)
                .clients(clients)
                .baseKey(tuple(baseKey))
                .build();
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
        return newArray(newArray(shardData, algorithm), input);
    }

    private Mono<Long> readLongMono(Mono<Value> value) {
        return value.map(element -> reader.read(longType(), element));
    }

    private Flux<ModelType> readSpaceFlux(Mono<Value> value) {
        return value.flatMapMany(elements -> fromStream(elements.asArrayValue()
                .list()
                .stream()
                .map(element -> reader.read(spaceMetaType, element))));
    }

    private Mono<ModelType> readSpaceMono(Mono<Value> value) {
        return value.map(element -> reader.read(spaceMetaType, element));
    }
}
