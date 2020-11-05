package ru.art.refactored.storage.dao;

import lombok.Getter;
import org.apache.logging.log4j.Logger;
import org.tarantool.TarantoolClient;
import ru.art.refactored.configuration.TarantoolSpaceConfig;
import ru.art.refactored.configuration.TarantoolSpaceFormat;
import ru.art.refactored.configuration.TarantoolSpaceIndex;

import java.io.OutputStream;
import java.util.List;

import static ru.art.refactored.storage.dao.caller.TarantoolFunctionCaller.call;
import static lombok.AccessLevel.PRIVATE;
import static org.apache.logging.log4j.io.IoBuilder.forLogger;
import static ru.art.logging.LoggingModule.loggingModule;

public class TarantoolSpaceDao {
    private final static OutputStream loggerOutputStream = forLogger(loggingModule().getLogger(TarantoolSpaceDao.class))
            .buildOutputStream();
    @Getter(lazy = true, value = PRIVATE)
    private static final Logger logger = loggingModule().getLogger(TarantoolSpaceDao.class);
    private TarantoolClient client;

    public TarantoolSpaceDao(TarantoolClient client){
        this.client = client;
    }

    public void create(String space, TarantoolSpaceConfig config){
        call(client, "art.space.create", space, config.getConfig());
    }

    public void format(String space, TarantoolSpaceFormat format){
        call(client, "art.space.format", space, format.getFormat());
    }

    public void createIndex(String space, String name, TarantoolSpaceIndex index){
        call(client, "art.space.create_index", space, name, index.getIndex());
    }

    public Long count(String space){
        List<?> response = call(client,"art.space.count", space);
        return ((Number) response.get(0)).longValue();
    }

    public Long len(String space){
        List<?> response = call(client, "art.space.len", space);
        return ((Number) response.get(0)).longValue();
    }

    public Long schemaCount(String space){
        List<?> response = call(client,"art.space.schema_count", space);
        return ((Number) response.get(0)).longValue();
    }

    public Long schemaLen(String space){
        List<?> response = call(client, "art.space.schema_len", space);
        return ((Number) response.get(0)).longValue();
    }

    public void rename(String space, String newName){
        call(client, "art.space.rename", space, newName);
    }

    public void truncate(String space){
        call(client, "art.space.truncate", space);
    }

    public void drop(String space){
        call(client, "art.space.drop", space);
    }

}
