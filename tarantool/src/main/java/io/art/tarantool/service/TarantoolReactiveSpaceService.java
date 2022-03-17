package io.art.tarantool.service;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.meta.model.*;
import io.art.storage.index.*;
import io.art.storage.service.*;
import io.art.storage.updater.*;
import io.art.tarantool.descriptor.*;
import io.art.tarantool.registry.*;
import io.art.tarantool.serializer.*;
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
public class TarantoolReactiveSpaceService<KeyType, ModelType> implements ReactiveSpaceService<KeyType, ModelType> {
    private final TarantoolModelReader reader;
    private final TarantoolUpdateSerializer updateSerializer;
    private final ImmutableStringValue spaceName;
    private final MetaType<ModelType> spaceMetaType;
    private final TarantoolClientRegistry clients;
    private final TarantoolModelWriter writer;
    private final MetaType<KeyType> keyMeta;

    public TarantoolReactiveSpaceService(MetaType<KeyType> keyMeta, MetaClass<ModelType> spaceMeta, TarantoolClientRegistry clients) {
        this.clients = clients;
        this.spaceMetaType = spaceMeta.definition();
        this.keyMeta = keyMeta;
        this.spaceName = newString(idByDash(spaceMeta.definition().type()));
        writer = tarantoolModule().configuration().getWriter();
        reader = tarantoolModule().configuration().getReader();
        updateSerializer = new TarantoolUpdateSerializer(writer);
    }

    @Override
    public Mono<ModelType> first(KeyType key) {
        ArrayValue input = newArray(spaceName, writer.write(keyMeta, key));
        Mono<Value> output = clients.immutable().call(SPACE_FIRST, input);
        return parseSpaceMono(output);
    }

    @Override
    public Flux<ModelType> select(KeyType key) {
        ArrayValue input = newArray(spaceName, writer.write(keyMeta, key));
        Mono<Value> output = clients.immutable().call(SPACE_SELECT, input);
        return parseSpaceFlux(output);
    }

    @Override
    public Flux<ModelType> select(KeyType key, long offset, long limit) {
        ArrayValue input = newArray(spaceName, writer.write(keyMeta, key), newArray(newInteger(offset), newInteger(limit)));
        Mono<Value> output = clients.immutable().call(SPACE_SELECT, input);
        return parseSpaceFlux(output);
    }

    @Override
    public Flux<ModelType> find(Collection<KeyType> keys) {
        ArrayValue input = newArray(spaceName, newArray(keys.stream().map(key -> writer.write(keyMeta, key)).collect(listCollector())));
        Mono<Value> output = clients.immutable().call(SPACE_FIND, input);
        return parseSpaceFlux(output);
    }

    @Override
    public Flux<ModelType> find(ImmutableCollection<KeyType> keys) {
        ArrayValue input = newArray(spaceName, newArray(keys.stream().map(key -> writer.write(keyMeta, key)).collect(listCollector())));
        Mono<Value> output = clients.immutable().call(SPACE_FIND, input);
        return parseSpaceFlux(output);
    }


    @Override
    public Mono<ModelType> delete(KeyType key) {
        ArrayValue input = newArray(spaceName, writer.write(keyMeta, key));
        Mono<Value> output = clients.immutable().call(SPACE_SINGLE_DELETE, input);
        return parseSpaceMono(output);
    }

    @Override
    public Flux<ModelType> delete(Collection<KeyType> keys) {
        ArrayValue input = newArray(spaceName, newArray(keys.stream().map(key -> writer.write(keyMeta, key)).collect(listCollector())));
        Mono<Value> output = clients.immutable().call(SPACE_MULTIPLE_DELETE, input);
        return parseSpaceFlux(output);
    }

    @Override
    public Flux<ModelType> delete(ImmutableCollection<KeyType> keys) {
        ArrayValue input = newArray(spaceName, newArray(keys.stream().map(key -> writer.write(keyMeta, key)).collect(listCollector())));
        Mono<Value> output = clients.immutable().call(SPACE_MULTIPLE_DELETE, input);
        return parseSpaceFlux(output);
    }


    @Override
    public Mono<ModelType> insert(ModelType value) {
        ArrayValue input = newArray(spaceName, writer.write(spaceMetaType, value));
        Mono<Value> output = clients.mutable().call(SPACE_SINGLE_INSERT, input);
        return parseSpaceMono(output);
    }

    @Override
    public Flux<ModelType> insert(Collection<ModelType> value) {
        ArrayValue input = newArray(spaceName, newArray(value.stream().map(element -> writer.write(spaceMetaType, element)).collect(listCollector())));
        Mono<Value> output = clients.mutable().call(SPACE_MULTIPLE_INSERT, input);
        return parseSpaceFlux(output);
    }

    @Override
    public Flux<ModelType> insert(ImmutableCollection<ModelType> value) {
        ArrayValue input = newArray(spaceName, newArray(value.stream().map(element -> writer.write(spaceMetaType, element)).collect(listCollector())));
        Mono<Value> output = clients.mutable().call(SPACE_MULTIPLE_INSERT, input);
        return parseSpaceFlux(output);
    }


    @Override
    public Mono<ModelType> put(ModelType value) {
        ArrayValue input = newArray(spaceName, writer.write(spaceMetaType, value));
        Mono<Value> output = clients.mutable().call(SPACE_SINGLE_PUT, input);
        return parseSpaceMono(output);
    }

    @Override
    public Flux<ModelType> put(Collection<ModelType> value) {
        ArrayValue input = newArray(spaceName, newArray(value.stream().map(element -> writer.write(spaceMetaType, element)).collect(listCollector())));
        Mono<Value> output = clients.mutable().call(SPACE_MULTIPLE_PUT, input);
        return parseSpaceFlux(output);
    }

    @Override
    public Flux<ModelType> put(ImmutableCollection<ModelType> value) {
        ArrayValue input = newArray(spaceName, newArray(value.stream().map(element -> writer.write(spaceMetaType, element)).collect(listCollector())));
        Mono<Value> output = clients.mutable().call(SPACE_MULTIPLE_PUT, input);
        return parseSpaceFlux(output);
    }

    @Override
    public Mono<ModelType> update(KeyType key, Updater<ModelType> updater) {
        ArrayValue input = newArray(spaceName, writer.write(keyMeta, key), updateSerializer.serializeUpdate(cast(updater)));
        Mono<Value> output = clients.mutable().call(SPACE_SINGLE_UPDATE, input);
        return parseSpaceMono(output);
    }

    @Override
    public Mono<Void> upsert(ModelType model, Updater<ModelType> updater) {
        ArrayValue input = newArray(spaceName, writer.write(spaceMetaType, model), updateSerializer.serializeUpdate(cast(updater)));
        Mono<Value> output = clients.mutable().call(SPACE_SINGLE_UPSERT, input);
        return output.then();
    }

    @Override
    public Flux<ModelType> update(Collection<KeyType> keys, Updater<ModelType> updater) {
        List<Value> serializedKeys = keys.stream().map(key -> writer.write(keyMeta, key)).collect(listCollector());
        ArrayValue input = newArray(spaceName, newArray(serializedKeys), updateSerializer.serializeUpdate(cast(updater)));
        Mono<Value> output = clients.mutable().call(SPACE_MULTIPLE_UPDATE, input);
        return parseSpaceFlux(output);
    }

    @Override
    public Flux<ModelType> update(ImmutableCollection<KeyType> keys, Updater<ModelType> updater) {
        List<Value> serializedKeys = keys.stream().map(key -> writer.write(keyMeta, key)).collect(listCollector());
        ArrayValue input = newArray(spaceName, newArray(serializedKeys), updateSerializer.serializeUpdate(cast(updater)));
        Mono<Value> output = clients.mutable().call(SPACE_MULTIPLE_UPDATE, input);
        return parseSpaceFlux(output);
    }

    @Override
    public Mono<Long> count(KeyType key) {
        Mono<Value> output = clients.immutable().call(SPACE_COUNT, newArray(spaceName, writer.write(keyMeta, key)));
        return parseLongMono(output);
    }

    @Override
    public Mono<Long> size() {
        Mono<Value> output = clients.immutable().call(SPACE_COUNT, newArray(spaceName));
        return parseLongMono(output);
    }

    @Override
    public Mono<Void> truncate() {
        return clients.mutable().call(SPACE_TRUNCATE, newArray(spaceName)).then();
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

    @Override
    public final ReactiveIndexService<ModelType> index(Index index) {
        return TarantoolReactiveIndexService.<ModelType>builder()
                .indexName(newString(index.name()))
                .spaceType(spaceMetaType)
                .fields(cast(index.fields()))
                .clients(clients)
                .spaceName(spaceName)
                .build();
    }

    private Mono<Long> parseLongMono(Mono<Value> value) {
        return value.map(element -> reader.read(longType(), element));
    }

    private Flux<ModelType> parseSpaceFlux(Mono<Value> value) {
        return value.flatMapMany(elements -> fromStream(elements.asArrayValue()
                .list()
                .stream()
                .map(element -> reader.read(spaceMetaType, element))));
    }

    private Mono<ModelType> parseSpaceMono(Mono<Value> value) {
        return value.map(element -> reader.read(spaceMetaType, element));
    }
}
