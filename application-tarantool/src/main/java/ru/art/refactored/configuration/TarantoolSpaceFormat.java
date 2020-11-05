package ru.art.refactored.configuration;

import lombok.NoArgsConstructor;

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

    public TarantoolSpaceFormat addField(String name, String type){
        Map<String, String> field = new LinkedHashMap<>();
        field.put("name", name);
        field.put("type", type);
        fields.add(field);
        return this;
    }

    public TarantoolSpaceFormat addField(String name, String type, Boolean isNullable){
        Map<String, Object> field = new LinkedHashMap<>();
        field.put("name", name);
        field.put("type", type);
        field.put("is_nullable", isNullable);
        fields.add(field);
        return this;
    }

    public List<Object> getFormat(){
        return fields;
    }

}
