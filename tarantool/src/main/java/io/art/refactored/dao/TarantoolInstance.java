package io.art.refactored.dao;

import lombok.*;
import org.apache.logging.log4j.*;
import org.tarantool.*;
import io.art.refactored.configuration.space.*;
import static io.art.logging.LoggingModule.*;
import static lombok.AccessLevel.*;
import static io.art.refactored.constants.TarantoolModuleConstants.Functions.*;
import static io.art.refactored.dao.caller.TarantoolFunctionCaller.*;

public class TarantoolInstance {
    @Getter(lazy = true, value = PRIVATE)
    private static final Logger logger = logger(TarantoolInstance.class);
    private TarantoolClient client;

    public TarantoolInstance(TarantoolClient client){
        this.client = client;
    }

    public void createSpace(String space, TarantoolSpaceConfig config){
        call(client, CREATE_SPACE, space, config.getConfig());
    }

    public void formatSpace(String space, TarantoolSpaceFormat format){
        call(client, FORMAT_SPACE, space, format.getFormat());
    }

    public void createIndex(String space, String name, TarantoolSpaceIndex index){
        call(client, CREATE_INDEX, space, name, index.getIndex());
    }
    
    public void renameSpace(String space, String newName){
        call(client, RENAME_SPACE, space, newName);
    }

    public void dropSpace(String space){
        call(client, DROP_SPACE, space);
    }

}
