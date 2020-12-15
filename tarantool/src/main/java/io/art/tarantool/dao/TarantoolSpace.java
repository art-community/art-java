package io.art.tarantool.dao;

import io.art.value.immutable.Value;
import io.tarantool.driver.api.TarantoolClient;
import lombok.*;
import org.apache.logging.log4j.*;
import io.art.tarantool.exception.*;
import io.art.tarantool.model.*;


import static io.art.logging.LoggingModule.*;
import static lombok.AccessLevel.*;

import java.util.*;

public class TarantoolSpace {
    @Getter(lazy = true, value = PRIVATE)
    private static final Logger logger = logger(TarantoolSpace.class);
    private TarantoolAsynchronousSpace asynchronousSpace;

    public TarantoolSpace(TarantoolClient client, String space){
        this.asynchronousSpace = new TarantoolAsynchronousSpace(client, space);
    }

    public Optional<Value> get(Value key){
        try{
            return asynchronousSpace.get(key).get();
        } catch (Exception e) {
            throw new TarantoolDaoException(e.getMessage());
        }
    }

    public Optional<Value> get(String index, Value key){
        try{
            return asynchronousSpace.get(index, key).get();
        } catch (Exception e) {
            throw new TarantoolDaoException(e.getMessage());
        }
    }

    public Optional<List<Value>> select(Value request){
        try{
            return asynchronousSpace.select(request).get();
        } catch (Exception e) {
            throw new TarantoolDaoException(e.getMessage());
        }
    }

    public Optional<List<Value>> select(String index, Value request){
        try{
            return asynchronousSpace.select(index, request).get();
        } catch (Exception e) {
            throw new TarantoolDaoException(e.getMessage());
        }
    }

    public Optional<Value> delete(Value key){
        try{
            return asynchronousSpace.delete(key).get();
        } catch (Exception e) {
            throw new TarantoolDaoException(e.getMessage());
        }
    }

    public Optional<Value> insert(Value data){
        try{
            return asynchronousSpace.insert(data).get();
        } catch (Exception e) {
            throw new TarantoolDaoException(e.getMessage());
        }
    }

    public Optional<Value> autoIncrement(Value data){
        try{
            return asynchronousSpace.autoIncrement(data).get();
        } catch (Exception e) {
            throw new TarantoolDaoException(e.getMessage());
        }
    }

    public Optional<Value> put(Value data){
        try{
            return asynchronousSpace.put(data).get();
        } catch (Exception e) {
            throw new TarantoolDaoException(e.getMessage());
        }
    }

    public Optional<Value> replace(Value data){
        try{
            return asynchronousSpace.replace(data).get();
        } catch (Exception e) {
            throw new TarantoolDaoException(e.getMessage());
        }
    }

    public Optional<Value> update(Value key, TarantoolUpdateFieldOperation... operations){
        try{
            return asynchronousSpace.update(key, operations).get();
        } catch (Exception e) {
            throw new TarantoolDaoException(e.getMessage());
        }
    }

    public Optional<Value> upsert(Value defaultValue, TarantoolUpdateFieldOperation... operations){
        try{
            return asynchronousSpace.upsert(defaultValue, operations).get();
        } catch (Exception e) {
            throw new TarantoolDaoException(e.getMessage());
        }
    }

    public Long count(){
        try{
            return asynchronousSpace.count().get();
        } catch (Exception e) {
            throw new TarantoolDaoException(e.getMessage());
        }
    }

    public Long len(){
        try{
            return asynchronousSpace.len().get();
        } catch (Exception e) {
            throw new TarantoolDaoException(e.getMessage());
        }
    }

    public Long schemaCount(){
        try{
            return asynchronousSpace.schemaCount().get();
        } catch (Exception e) {
            throw new TarantoolDaoException(e.getMessage());
        }
    }

    public Long schemaLen(){
        try{
            return asynchronousSpace.schemaLen().get();
        } catch (Exception e) {
            throw new TarantoolDaoException(e.getMessage());
        }
    }

    public void truncate(){
        asynchronousSpace.truncate();
    }

    public Set<String> listIndices(){
        try{
            return asynchronousSpace.listIndices().get();
        } catch (Exception e) {
            throw new TarantoolDaoException(e.getMessage());
        }
    }

}
