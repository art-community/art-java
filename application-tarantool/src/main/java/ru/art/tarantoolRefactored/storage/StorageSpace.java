package ru.art.tarantoolRefactored.storage;

import ru.art.entity.Value;

import java.util.List;
import java.util.Optional;

public interface StorageSpace {

    public Optional<Value> get(Value key);

    public Optional<List<Value>> find(Value request);

    public Optional<Value> insert(Value data);

    public Optional<Value> autoIncrement(Value data);

    public Optional<Value> put(Value data);

    public Optional<Value> delete(Value key);

    public void truncate();

    public Long count();

}