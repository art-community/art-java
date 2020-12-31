package io.art.tarantool.storage;

import io.art.tarantool.space.TarantoolSpace;
import io.art.value.immutable.*;

import java.util.*;

public class TarantoolStorageSpace implements StorageSpace{
    private final TarantoolSpace space;

    public TarantoolStorageSpace(TarantoolSpace space){
        this.space = space;
    }

    @Override
    public Optional<Value> get(Value key) {
        return space.get(key).getOptional();
    }

    @Override
    public Optional<List<Value>> find(Value request){
        return space.select(request).fetch().getOptional();
    }

    @Override
    public Optional<Value> delete(Value key){
        return space.delete(key).getOptional();
    }

    @Override
    public Optional<Value> insert(Value data) {
        return space.insert(data).getOptional();
    }

    @Override
    public Optional<Value> autoIncrement(Value data) {
        return space.autoIncrement(data).getOptional();
    }

    @Override
    public Optional<Value> put(Value data) {
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
