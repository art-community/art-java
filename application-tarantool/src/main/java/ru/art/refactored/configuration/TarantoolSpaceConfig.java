package ru.art.refactored.configuration;

import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

@NoArgsConstructor(staticName = "tarantoolSpaceConfig")
public class TarantoolSpaceConfig {
    private Map<String, Object> config = new LinkedHashMap<>();

    public TarantoolSpaceConfig engine(String engine){
        config.put("engine", engine);
        return this;
    }

    public TarantoolSpaceConfig fieldCount(Integer fieldCount){
        config.put("field_count", fieldCount);
        return this;
    }

    public TarantoolSpaceConfig format(TarantoolSpaceFormat format){
        config.put("format", format.getFormat());
        return this;
    }

    public TarantoolSpaceConfig id(Integer id){
        config.put("id", id);
        return this;
    }

    public TarantoolSpaceConfig ifNotExists(Boolean ifNotExists){
        config.put("if_not_exists", ifNotExists);
        return this;
    }

    public TarantoolSpaceConfig isLocal(Boolean isLocal){
        config.put("is_local", isLocal);
        return this;
    }

    public TarantoolSpaceConfig isTemporary(boolean isTemporary){
        config.put("temporary", isTemporary);
        return this;
    }

    public TarantoolSpaceConfig user(String user){
        config.put("user", user);
        return this;
    }

    public Map<String, Object> getConfig(){
        return config;
    }

}
