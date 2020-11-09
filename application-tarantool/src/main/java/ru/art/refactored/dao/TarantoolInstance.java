package ru.art.refactored.dao;

import lombok.Getter;
import org.apache.logging.log4j.Logger;
import org.tarantool.TarantoolClient;
import ru.art.refactored.configuration.space.TarantoolSpaceConfig;
import ru.art.refactored.configuration.space.TarantoolSpaceFormat;
import ru.art.refactored.configuration.space.TarantoolSpaceIndex;

import static ru.art.refactored.dao.caller.TarantoolFunctionCaller.call;
import static lombok.AccessLevel.PRIVATE;
import static ru.art.logging.LoggingModule.loggingModule;

public class TarantoolInstance {
    @Getter(lazy = true, value = PRIVATE)
    private static final Logger logger = loggingModule().getLogger(TarantoolInstance.class);
    private TarantoolClient client;

    public TarantoolInstance(TarantoolClient client){
        this.client = client;
    }

    public void createSpace(String space, TarantoolSpaceConfig config){
        call(client, "art.space.create", space, config.getConfig());
    }

    public void formatSpace(String space, TarantoolSpaceFormat format){
        call(client, "art.space.format", space, format.getFormat());
    }

    public void createIndex(String space, String name, TarantoolSpaceIndex index){
        call(client, "art.space.create_index", space, name, index.getIndex());
    }
    
    public void renameSpace(String space, String newName){
        call(client, "art.space.rename", space, newName);
    }

    public void dropSpace(String space){
        call(client, "art.space.drop", space);
    }

}
