package io.art.tarantool.dao;

import io.tarantool.driver.api.TarantoolClient;
import lombok.*;
import org.apache.logging.log4j.*;
import io.art.tarantool.configuration.space.*;
import java.util.List;
import java.util.Set;
import static io.art.core.caster.Caster.cast;
import static io.art.logging.LoggingModule.*;
import static lombok.AccessLevel.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.Functions.*;
import static io.art.tarantool.caller.TarantoolFunctionCaller.*;
import static io.art.core.factory.SetFactory.setOf;

public class TarantoolInstance {
    @Getter(lazy = true, value = PRIVATE)
    private static final Logger logger = logger(TarantoolInstance.class);
    private final TarantoolClient client;

    public TarantoolInstance(TarantoolClient client){
        this.client = client;
    }

    public Set<String> listSpaces(){
        List<String> response = cast(call(client, LIST_SPACES).get(0));
        return setOf(response);
    }

    public TarantoolSpace getSpace(String space){
        return new TarantoolSpace(client, space);
    }

    public TarantoolAsyncSpace getAsyncSpace(String space){
        return new TarantoolAsyncSpace(client, space);
    }

    public void createSpace(String space, TarantoolSpaceConfig config){
        call(client, CREATE_SPACE, space, config.getConfig());
    }

    public void formatSpace(String space, TarantoolSpaceFormat format){
        call(client, FORMAT_SPACE, space, format.getFormat());
    }

    public void createIndex(String space, String indexName, TarantoolSpaceIndex indexConfig){
        call(client, CREATE_INDEX, space, indexName, indexConfig.getIndex());
    }

    public void dropIndex(String space, String indexName){
        call(client, DROP_INDEX, space, indexName);
    }
    
    public void renameSpace(String space, String newName){
        call(client, RENAME_SPACE, space, newName);
    }

    public void dropSpace(String space){
        call(client, DROP_SPACE, space);
    }

}
