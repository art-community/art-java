package ru.art.refactored.configuration.space;


import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import static ru.art.refactored.constants.TarantoolModuleConstants.TarantoolIndexType;
import java.util.*;

@NoArgsConstructor(staticName = "tarantoolSpaceIndex")
@Accessors(chain = true, fluent = true)
public class TarantoolSpaceIndex {
    private Map<String, Object> index = new LinkedHashMap<>();
    private List<Object> parts = new ArrayList<>();

    public TarantoolSpaceIndex type(TarantoolIndexType type) {
        index.put("type", type.toString());
        return this;
    }

    public TarantoolSpaceIndex part(String name){
        parts.add(name);
        return this;
    }

    public TarantoolSpaceIndex part(Integer number){
        parts.add(number);
        return this;
    }

    public TarantoolSpaceIndex id(Long id){
        index.put("id", id);
        return this;
    }

    public TarantoolSpaceIndex unique(Boolean isUnique){
        index.put("unique", isUnique);
        return this;
    }

    public TarantoolSpaceIndex ifNotExists(Boolean ifNotExists){
        index.put("if_not_exists", ifNotExists);
        return this;
    }

    public TarantoolSpaceIndex dimension(Integer count){
        index.put("dimension", count);
        return this;
    }

    public TarantoolSpaceIndex distance(String method){
        index.put("distance", method);
        return this;
    }

    public TarantoolSpaceIndex bloomFpr(Double rate){
        index.put("bloom_fpr", rate);
        return this;
    }

    public TarantoolSpaceIndex pageSize(Long bytes){
        index.put("page_size", bytes);
        return this;
    }

    public TarantoolSpaceIndex rangeSize(Long size){
        index.put("range_size", size);
        return this;
    }

    public TarantoolSpaceIndex runCountPerLevel(Long count){
        index.put("run_count_per_level", count);
        return this;
    }

    public TarantoolSpaceIndex runSizeRatio(Double ratio){
        index.put("run_size_ratio", ratio);
        return this;
    }

    public TarantoolSpaceIndex sequence(String sequence){
        index.put("sequence", sequence);
        return this;
    }

    public TarantoolSpaceIndex sequence(Long sequence){
        index.put("sequence", sequence);
        return this;
    }

    public TarantoolSpaceIndex func(String func){
        index.put("func", func);
        return this;
    }

    public Map<String, Object> getIndex(){
        index.put("parts", parts);
        return index;
    }

}
