package io.art.tarantool.dao;

import io.art.tarantool.module.client.TarantoolClusterClient;
import lombok.*;
import org.apache.logging.log4j.*;
import io.art.tarantool.configuration.space.*;
import java.util.List;
import java.util.Set;
import static io.art.core.caster.Caster.cast;
import static io.art.logging.LoggingModule.*;
import static lombok.AccessLevel.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.Functions.*;
import static io.art.core.factory.SetFactory.setOf;

public class TarantoolInstance {
    @Getter(lazy = true, value = PRIVATE)
    private static final Logger logger = logger(TarantoolInstance.class);
    private final TarantoolClusterClient client;

    public TarantoolInstance(TarantoolClusterClient client){
        this.client = client;
    }

    public Set<String> listSpaces(){
        List<String> response = cast( client.callRO(LIST_SPACES).get(0) );
        return setOf(response);
    }

    public TarantoolSpace space(String space){
        return new TarantoolSpace(client, space);
    }

    public TarantoolAsynchronousSpace asynchronousSpace(String space){
        return new TarantoolAsynchronousSpace(client, space);
    }

    public void createSpace(String space, TarantoolSpaceConfig config){
        client.callRW(CREATE_SPACE, space, config.getConfig());
    }

    public void formatSpace(String space, TarantoolSpaceFormat format){
        client.callRW(FORMAT_SPACE, space, format.getFormat());
    }

    public void createIndex(String space, String indexName, TarantoolSpaceIndex indexConfig){
        client.callRW(CREATE_INDEX, space, indexName, indexConfig.getIndex());
    }

    public void dropIndex(String space, String indexName){
        client.callRW(DROP_INDEX, space, indexName);
    }
    
    public void renameSpace(String space, String newName){
        client.callRW(RENAME_SPACE, space, newName);
    }

    public void dropSpace(String space){
        client.callRW(DROP_SPACE, space);
    }

}
