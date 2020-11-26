package ru.art.tarantoolRefactored.dao;

import ru.art.entity.Value;

import java.util.Optional;

public class TarantoolSpaceIndex {
    private TarantoolSpace space;
    private String name;

    public TarantoolSpaceIndex(TarantoolSpace space, String indexName){
        this.space = space;
        name = indexName;
    }

    public Optional<Value> get(Value key){
        return Optional.empty();
    }



    //select get min max random count update delete alter drop rename
}
