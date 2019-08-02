package ru.art.tarantool.model;

import lombok.Builder;
import lombok.Getter;
import java.util.Map;

@Getter
@Builder(buildMethodName = "map", builderMethodName = "entityFieldsMapping")
public class TarantoolEntityFieldsMapping {
    private final Map<String, Integer> fieldsMapping;

    public int map(String fieldName) {
        return fieldsMapping.get(fieldName);
    }
}