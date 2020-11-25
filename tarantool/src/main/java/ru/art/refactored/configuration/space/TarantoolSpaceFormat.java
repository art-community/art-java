package ru.art.refactored.configuration.space;

import lombok.NoArgsConstructor;
import static ru.art.refactored.constants.TarantoolModuleConstants.TarantoolFieldType;

import java.util.*;

@NoArgsConstructor(staticName = "tarantoolSpaceFormat")
public class TarantoolSpaceFormat {
    private List<Object> fields = new ArrayList<>();

    public TarantoolSpaceFormat addField(String name){
        Map<String, String> field = new LinkedHashMap<>();
        field.put("name", name);
        fields.add(field);
        return this;
    }

    public TarantoolSpaceFormat addField(String name, TarantoolFieldType type){
        Map<String, String> field = new LinkedHashMap<>();
        field.put("name", name);
        field.put("type", type.toString());
        fields.add(field);
        return this;
    }

    public TarantoolSpaceFormat addField(String name, TarantoolFieldType type, Boolean isNullable){
        Map<String, Object> field = new LinkedHashMap<>();
        field.put("name", name);
        field.put("type", type.toString());
        field.put("is_nullable", isNullable);
        fields.add(field);
        return this;
    }

    public List<Object> getFormat(){
        return fields;
    }

}
