package io.art.tarantool.storage;

import io.art.core.collection.ImmutableArray;
import io.art.tarantool.space.TarantoolSpace;
import io.art.value.immutable.*;
import io.art.storage.space.StorageSpace;

import java.util.*;

public class TarantoolStorageSpace<T> implements StorageSpace<T>{
    private final TarantoolSpace<T> space;

    public TarantoolStorageSpace(TarantoolSpace<T> space){
        this.space = space;
    }

    @Override
    public Optional<T> get(Value key) {
        return space.get(key).getOptional();
    }

    @Override
    public ImmutableArray<T> find(Value request){
        return space.select(request).get();
    }

    @Override
    public Optional<T> delete(Value key){
        return space.delete(key).getOptional();
    }

    @Override
    public Optional<T> insert(Value data) {
        return space.insert(data).getOptional();
    }

    @Override
    public Optional<T> put(Value data) {
        return space.put(data).getOptional();
    }

    @Override
    public void truncate(){
        space.truncate();
    }

    @Override
    public Optional<Long> count(){
        return space.count().getOptional();
    }

}
