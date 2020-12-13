package io.art.tarantool.dao;

import io.art.value.immutable.Value;
import io.tarantool.driver.api.TarantoolClient;
import lombok.*;
import org.apache.logging.log4j.*;

import io.art.tarantool.exception.*;
import io.art.tarantool.model.*;

import static io.art.core.factory.CollectionsFactory.setOf;
import static io.art.logging.LoggingModule.*;
import static lombok.AccessLevel.*;

import java.util.*;

public class TarantoolSpace {
    @Getter(lazy = true, value = PRIVATE)
    private static final Logger logger = logger(TarantoolSpace.class);
    private TarantoolAsyncSpace asyncSpace;

    public TarantoolSpace(TarantoolClient client, String space){
        this.asyncSpace = new TarantoolAsyncSpace(client, space);
    }

    public Optional<Value> get(Value key){
        try{
            return asyncSpace.get(key).get();
        } catch (Exception e) {
            throw new TarantoolDaoException(e.getMessage());
        }
    }

    public Optional<Value> get(String index, Value key){
        try{
            return asyncSpace.get(index, key).get();
        } catch (Exception e) {
            throw new TarantoolDaoException(e.getMessage());
        }
    }

    public Optional<List<Value>> select(Value request){
        try{
            return asyncSpace.select(request).get();
        } catch (Exception e) {
            throw new TarantoolDaoException(e.getMessage());
        }
    }

    public Optional<List<Value>> select(String index, Value request){
        try{
            return asyncSpace.select(index, request).get();
        } catch (Exception e) {
            throw new TarantoolDaoException(e.getMessage());
        }
    }

    public Optional<Value> delete(Value key){
        try{
            return asyncSpace.delete(key).get();
        } catch (Exception e) {
            throw new TarantoolDaoException(e.getMessage());
        }
    }

    public Optional<Value> insert(Value data){
        try{
            return asyncSpace.insert(data).get();
        } catch (Exception e) {
            throw new TarantoolDaoException(e.getMessage());
        }
    }

    public Optional<Value> autoIncrement(Value data){
        try{
            return asyncSpace.autoIncrement(data).get();
        } catch (Exception e) {
            throw new TarantoolDaoException(e.getMessage());
        }
    }

    public Optional<Value> put(Value data){
        try{
            return asyncSpace.put(data).get();
        } catch (Exception e) {
            throw new TarantoolDaoException(e.getMessage());
        }
    }

    public Optional<Value> replace(Value data){
        try{
            return asyncSpace.replace(data).get();
        } catch (Exception e) {
            throw new TarantoolDaoException(e.getMessage());
        }
    }

    public Optional<Value> update(Value key, TarantoolUpdateFieldOperation... operations){
        try{
            return asyncSpace.update(key, operations).get();
        } catch (Exception e) {
            throw new TarantoolDaoException(e.getMessage());
        }
    }

    public Optional<Value> upsert(Value defaultValue, TarantoolUpdateFieldOperation... operations){
        try{
            return asyncSpace.upsert(defaultValue, operations).get();
        } catch (Exception e) {
            throw new TarantoolDaoException(e.getMessage());
        }
    }

    public Long count(){
        try{
            return asyncSpace.count().get();
        } catch (Exception e) {
            throw new TarantoolDaoException(e.getMessage());
        }
    }

    public Long len(){
        try{
            return asyncSpace.len().get();
        } catch (Exception e) {
            throw new TarantoolDaoException(e.getMessage());
        }
    }

    public Long schemaCount(){
        try{
            return asyncSpace.schemaCount().get();
        } catch (Exception e) {
            throw new TarantoolDaoException(e.getMessage());
        }
    }

    public Long schemaLen(){
        try{
            return asyncSpace.schemaLen().get();
        } catch (Exception e) {
            throw new TarantoolDaoException(e.getMessage());
        }
    }

    public void truncate(){
        asyncSpace.truncate();
    }

    public Set<String> listIndices(){
        try{
            return asyncSpace.listIndices().get();
        } catch (Exception e) {
            throw new TarantoolDaoException(e.getMessage());
        }
    }

}
