package io.art.tarantool.storage;

import io.art.value.immutable.*;
import io.art.tarantool.dao.*;
import java.util.*;

public class TarantoolStorageSpace implements StorageSpace{
    private final TarantoolSpace space;

    public TarantoolStorageSpace(TarantoolSpace space){
        this.space = space;
    }

    @Override
    public Optional<Value> get(Value key) {
        return space.get(key);
    }

    @Override
    public Optional<List<Value>> find(Value request){
        return space.select(request);
    }

    @Override
    public Optional<Value> delete(Value key){
        return space.delete(key);
    }

    @Override
    public Optional<Value> insert(Value data) {
        return space.insert(data);
    }

    @Override
    public Optional<Value> autoIncrement(Value data) {
        return space.autoIncrement(data);
    }

    @Override
    public Optional<Value> put(Value data) {
        return space.put(data);
    }

    @Override
    public void truncate(){
        space.truncate();
    }

    @Override
    public Long count(){
        return space.count();
    }

}
